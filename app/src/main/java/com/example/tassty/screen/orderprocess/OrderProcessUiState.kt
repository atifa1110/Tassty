package com.example.tassty.screen.orderprocess

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.RouteOrderUiModel
import com.google.android.gms.maps.model.LatLng

data class OrderProcessUiState(
    val detail: Resource<DetailOrderUiModel> = Resource(),
    val route: Resource<RouteOrderUiModel> = Resource(),
    val driverPosition: LatLng? = null,
    val isArrivedModalVisible: Boolean = false,
    val currentStepIndex: Int? = null,
    val currentStatus: OrderStatus? = null,
    val isSimulated: Boolean = false
)

data class OrderProcessInternalUiState (
    val isArrivedModalVisible: Boolean = false,
    val currentStepIndex: Int? = null,
    val isSimulated: Boolean = false
)

sealed interface OrderProcessEvent {
    data class NavigateToMessage(val channelId: String) : OrderProcessEvent
    data object NavigateBack : OrderProcessEvent
    data class ShowMessage(val message: String) : OrderProcessEvent
}
