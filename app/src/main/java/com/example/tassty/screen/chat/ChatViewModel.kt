package com.example.tassty.screen.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.DeleteAllNotificationsUseCase
import com.example.core.domain.usecase.DeleteChatChannelUseCase
import com.example.core.domain.usecase.GetChatCurrentUserIdUseCase
import com.example.core.domain.usecase.GetChatListChannelUseCase
import com.example.core.domain.usecase.GetNotificationsUseCase
import com.example.core.domain.usecase.MarkNotificationReadUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.ChatUiModel
import com.example.tassty.ChatTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatListChannelUseCase: GetChatListChannelUseCase,
    private val deleteChatChannelUseCase: DeleteChatChannelUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val deleteAllNotificationsUseCase: DeleteAllNotificationsUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(ChatInternalState())

    private val _uiEvent = Channel<ChatEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiState: StateFlow<ChatUiState> = combine(
        _internalState,
        getChatListChannelUseCase(),
        getNotificationsUseCase()
    ) { internal, chats, notifications ->
        ChatUiState(
            chats = chats.toListState { domainModel ->
                domainModel.toUiModel().copy(
                    isSwipeActionVisible = internal.revealedChatIds.contains(domainModel.id)
                )
            },
            notifications = notifications.toListState{ it.toUiModel() },
            isDeleteChatSheetVisible = internal.isDeleteChatSheetVisible,
            isDeleteNotifSheetVisible = internal.isDeleteNotifSheetVisible,
            selectedTab = internal.selectedTab,
            selectedChat = internal.selectedChat
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChatUiState()
    )

    fun onRevealChange(chatId: String, isRevealed: Boolean) {
        _internalState.update { state ->
            state.copy(
                revealedChatIds = if (isRevealed) {
                    setOf(chatId)
                } else {
                    emptySet()
                }
            )
        }
    }

    fun onSelectedChat(chat: ChatUiModel){
        _internalState.update { it.copy(selectedChat = chat, isDeleteChatSheetVisible = true) }
    }

    fun onNotificationClick(id: String) = viewModelScope.launch {
        markNotificationReadUseCase(id)
    }

    fun onDismissDelete() {
        _internalState.update { it.copy(isDeleteNotifSheetVisible = false) }
    }

    fun onDeleteClick() = viewModelScope.launch {
        val currentTab = uiState.value.selectedTab

        if (currentTab == ChatTab.NOTIFICATION) {
            val notifSize = uiState.value.notifications.data?.size ?: 0
            if (notifSize > 0) {
                _internalState.update { it.copy(isDeleteNotifSheetVisible = true) }
            } else {
                _uiEvent.send(ChatEvent.ShowMessage("There is no notification to delete"))
            }
        } else {
            _uiEvent.send(ChatEvent.ShowMessage("This feature is currently unavailable"))
        }
    }

    fun onDeleteAllNotification() = viewModelScope.launch {
        val deletedCount = deleteAllNotificationsUseCase()
        if(deletedCount>0){
            _uiEvent.send(ChatEvent.ShowMessage("All notification deleted"))
        }
        _internalState.update { it.copy(isDeleteNotifSheetVisible = false) }
    }

    fun onRemoveChat(){
        val channelId = _internalState.value.selectedChat?.id?: return
        viewModelScope.launch {
            deleteChatChannelUseCase(channelId).collect { result->
                when(result){
                    is TasstyResponse.Error -> {
                        _uiEvent.send(ChatEvent.ShowMessage(result.meta.message))
                    }
                    is TasstyResponse.Loading -> {}
                    is TasstyResponse.Success -> {
                        _internalState.update { it.copy(
                            selectedChat = null,
                            isDeleteChatSheetVisible = false,
                            revealedChatIds = emptySet()
                        ) }
                        _uiEvent.send(ChatEvent.ShowMessage(result.data?:""))
                    }
                }
            }
        }
    }

    fun onDismissDeleteChat(){
        _internalState.update {
            it.copy(
                selectedChat = null,
                isDeleteChatSheetVisible = false,
                revealedChatIds = emptySet()
            )
        }
    }

    fun onSelectedTab(tab : ChatTab){
        _internalState.update { it.copy(selectedTab = tab) }
    }
}