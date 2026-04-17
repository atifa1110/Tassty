package com.example.tassty.screen.message

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetChatMessagesUseCase
import com.example.core.domain.usecase.GetChatOtherUserUseCase
import com.example.core.domain.usecase.GetOrderSummaryUseCase
import com.example.core.domain.usecase.SendChatMessageUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.MessageUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.utils.mapToResource
import com.example.core.utils.toImmutableListState
import com.example.tassty.navigation.MessageDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val getChatOtherUserUseCase: GetChatOtherUserUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val getOrderSummaryUseCase: GetOrderSummaryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val channelId: String = MessageDestination.getId(savedStateHandle)
    private val orderId = channelId.substringAfter("messaging:")
        .substringAfter("order-")
    private val _internalState = MutableStateFlow(MessageInternalState())

    val groupedMessagesFlow = getChatMessagesUseCase(channelId)
        .map { response ->
            response.toImmutableListState { it.toUiModel() }.let { listState ->
                val grouped = listState.data?.groupBy { it.date }?.mapValues { (_, items) ->
                    items.reversed().toImmutableList()
                }?.toImmutableMap() ?: persistentMapOf()
                Pair(listState.isLoading, grouped)
            }
        }
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)

    val uiState: StateFlow<MessageUiState> = combine(
        _internalState,
        getChatOtherUserUseCase(channelId).distinctUntilChanged(),
        groupedMessagesFlow,
        getOrderSummaryUseCase(orderId).distinctUntilChanged()
    ) { internal, otherUser, (msgLoading, grouped), order ->

        val orderRes = order.mapToResource { it.toUiModel() }

        MessageUiState(
            user = otherUser,
            groupedMessages = grouped,
            order = orderRes,
            placeholder = if (internal.isImagePreviewVisible) "Add a caption..." else "Write a message...",
            sendMessage = internal.sendMessage,
            selectedImageUri = internal.selectedImageUri,
            selectedMessage = internal.selectedMessage,
            isImagePreviewVisible = internal.isImagePreviewVisible,
            isImageSheetVisible = internal.isImageSheetVisible,
            isLoading = msgLoading || internal.isLoading || orderRes.isLoading,
            uploadProgress = internal.progress
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MessageUiState()
    )

    private val _events = MutableSharedFlow<MessageEvent>()
    val events: SharedFlow<MessageEvent> = _events.asSharedFlow()

    fun onTextChange(text: String) {
        _internalState.update {
            it.copy(sendMessage = text)
        }
    }

    fun onDismissImageSheet(){
        _internalState.update { it.copy(isImageSheetVisible = false) }
    }

    fun onImageClose() {
        _internalState.update { it.copy(selectedImageUri = null, isImagePreviewVisible = false, selectedMessage = null) }
    }

    fun onImageSelected(uri: Uri?) {
        _internalState.update { it.copy(selectedImageUri = uri, isImagePreviewVisible = true) }
    }

    fun onImageClick(message: MessageUiModel){
        _internalState.update { it.copy(isImageSheetVisible = true, selectedMessage = message) }
    }

    fun onSendMessage(){
        val order = uiState.value.order
        val currentText = _internalState.value.sendMessage
        val imageUri = _internalState.value.selectedImageUri

        if (order.data?.status == OrderStatus.COMPLETED) {
            viewModelScope.launch {
                _events.emit(MessageEvent.ShowMessage("Order is completed. You can't send messages anymore."))
            }
            return
        }

        viewModelScope.launch {
            sendChatMessageUseCase(channelId = channelId, message = currentText, imageUri = imageUri).collect { result->
                when(result){
                    is TasstyResponse.Error -> {
                        _events.emit(MessageEvent.ShowMessage(result.meta.message))
                    }
                    is TasstyResponse.Loading -> _internalState.update { it.copy(isLoading = true, progress = result.progress) }
                    is TasstyResponse.Success-> {
                        _internalState.update { it.copy(sendMessage = "", selectedImageUri = null, isLoading = false, progress = 0f) }
                    }
                }
            }
        }
    }
}