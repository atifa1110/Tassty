package com.example.tassty.screen.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetPaymentChannelUseCase
import com.example.core.domain.usecase.GetUserCardUseCase
import com.example.core.domain.usecase.ProcessOrderPaymentStripeUseCase
import com.example.core.domain.usecase.RemoveHiddenCardUseCase
import com.example.core.ui.utils.toListState
import com.example.core.ui.mapper.toUiModel
import com.example.tassty.navigation.PaymentDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getUserCardUseCase: GetUserCardUseCase,
    private val getPaymentChannelUseCase: GetPaymentChannelUseCase,
    private val processOrderPaymentStripeUseCase: ProcessOrderPaymentStripeUseCase,
    private val removeHiddenCardUseCase: RemoveHiddenCardUseCase
): ViewModel(){

    val id = PaymentDestination.getId(savedStateHandle)

    private val _navigation = MutableSharedFlow<PaymentEvent>()
    val navigation = _navigation.asSharedFlow()
    private val _internalState = MutableStateFlow(PaymentInternalState())
    val cardContent = getUserCardUseCase().map { it.toListState { it.toUiModel() } }
    val paymentContent = getPaymentChannelUseCase().map { it.toListState { it.toUiModel() } }

    val uiState: StateFlow<PaymentUiState> = combine(
        cardContent,
        paymentContent,
        _internalState
    ){card, payment, internal->

        val canProceed = internal.selectedCardId != null || internal.selectedChannelId != null

        val updatedCards = card.copy(
            data = card.data?.map {
                it.copy(isSelected = it.id == internal.selectedCardId)
            }
        )

        val updatedChannels = payment.copy(
            data = payment.data?.map {
                it.copy(isSelected = it.channelCode == internal.selectedChannelId)
            }
        )
        PaymentUiState(
            cardPayment = updatedCards,
            paymentChannel = updatedChannels,
            selectedCardId = internal.selectedCardId,
            selectedChannelId = internal.selectedChannelId,
            isButtonEnabled = canProceed,
            isLoading = internal.isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PaymentUiState()
    )

    fun onCardSelected(cardId: String) {
        _internalState.update { it.copy(selectedCardId = cardId, selectedChannelId = null) }
    }

    fun onChannelSelected(channelId: String) {
        _internalState.update { it.copy(selectedCardId = null, selectedChannelId = channelId) }
    }

    fun onSwipePayment(){
        val state = uiState.value
        val selectedCard = state.selectedCardId?: return
        val paymentMethod = state.cardPayment.data?.find { it.id == selectedCard }?.stripeId?:""
        viewModelScope.launch {
            processOrderPaymentStripeUseCase(orderId = id, paymentMethod = paymentMethod).collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(isLoading = false) }
                        _navigation.emit(PaymentEvent.OnShowError(result.meta.message))
                    }
                    is TasstyResponse.Loading -> {_internalState.update { it.copy(isLoading = true) }}
                    is TasstyResponse.Success -> {
                        removeHiddenCardUseCase()
                        _internalState.update { it.copy(isLoading = false) }
                        _navigation.emit(PaymentEvent.NavigateToOrderDetail(id))
                    }
                }
            }
        }
    }
}