package com.example.tassty.screen.payment

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CardUserUiModel
import com.example.core.ui.model.PaymentChannelUiModel
import com.example.tassty.screen.orders.OrderEvent

data class PaymentUiState(
    val cardPayment: Resource<List<CardUserUiModel>> = Resource(),
    val paymentChannel: Resource<List<PaymentChannelUiModel>> = Resource(),
    val selectedCardId: String? = null,
    val selectedChannelId: String? = null,
    val isButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
){
    val groupedPaymentChannels: Map<String, List<PaymentChannelUiModel>>
        get() = paymentChannel.data?.groupBy { it.channelCategory } ?: emptyMap()
}

data class PaymentInternalState(
    val selectedCardId: String? = null,
    val selectedChannelId: String? = null,
    val isLoading: Boolean = false
)

sealed class PaymentEvent {
    data class NavigateToOrderDetail(val id: String) : PaymentEvent()
    data class OnShowError(val message: String) : PaymentEvent()
}