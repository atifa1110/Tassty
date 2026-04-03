package com.example.tassty.screen.orderprocess

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.DetailOrderUiModel

data class OrderProcessUiState (
    val detail : Resource<DetailOrderUiModel> = Resource(),
    val isArrivedModalVisible: Boolean = false
)

data class OrderProcessInternalUiState (
    val isArrivedModalVisible: Boolean = false
)

sealed interface OrderProcessEvent {
    data class NavigateToMessage(val channelId: String) : OrderProcessEvent
    data class ShowMessage(val message: String) : OrderProcessEvent
}
