package com.example.tassty.screen.orderprocess

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.CreateChatChannelUseCase
import com.example.core.domain.usecase.GetDetailOrderUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.utils.mapToResource
import com.example.tassty.navigation.OrderProcessDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderProcessViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailOrderUseCase: GetDetailOrderUseCase,
    private val createChatChannelUseCase: CreateChatChannelUseCase
) : ViewModel() {

    val orderId = OrderProcessDestination.getId(savedStateHandle)

    private val _internalState = MutableStateFlow(OrderProcessInternalUiState())
    private val _events = MutableSharedFlow<OrderProcessEvent>()
    val events: SharedFlow<OrderProcessEvent> = _events.asSharedFlow()

    val uiState: StateFlow<OrderProcessUiState> = getDetailOrderUseCase(orderId)
        .combine(_internalState) { detailResource, internal ->
            val detailUi = detailResource.mapToResource { it.toUiModel() }

            if (detailUi.data?.status == OrderStatus.COMPLETED && !internal.isArrivedModalVisible) {
                showArrivedModal()
            }

            OrderProcessUiState(
                detail = detailUi,
                isArrivedModalVisible = internal.isArrivedModalVisible
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OrderProcessUiState()
        )

    private fun showArrivedModal() {
        viewModelScope.launch {
            delay(1000)
            _internalState.update { it.copy(isArrivedModalVisible = true) }
        }
    }

    fun onDismissModal() {
        _internalState.update { it.copy(isArrivedModalVisible = false) }
    }

    fun onChatClicked() = viewModelScope.launch {
        val currentDetail = uiState.value.detail.data
        val currentCid = currentDetail?.chatChannelId

        if (!currentCid.isNullOrEmpty()) {
            _events.emit(OrderProcessEvent.NavigateToMessage(currentCid))
            return@launch
        }

        createChatChannelUseCase(orderId).collect { resource ->
            when (resource) {
                is TasstyResponse.Loading -> {}
                is TasstyResponse.Success -> {
                    val channelId = resource.data ?: ""
                    _events.emit(OrderProcessEvent.NavigateToMessage(channelId))
                }
                is TasstyResponse.Error -> {
                    _events.emit(OrderProcessEvent.ShowMessage(resource.meta.message))
                }
            }
        }
    }
}