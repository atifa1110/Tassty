package com.example.tassty.screen.orders

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.OrderUiModel
import com.example.tassty.orderFilters
import com.example.tassty.screen.home.HomeEvent
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

data class OrderUiState (
    val orders: Resource<List<OrderUiModel>> = Resource(),
    val filter : List<FilterOptionUi<OrderFilterCategory>> = orderFilters,
    val selectedCategory: OrderFilterCategory = OrderFilterCategory.ALL,
    val isCalendarVisible: Boolean = false,
    val startDateSelected: LocalDate? = LocalDate.now(),
    val endDateSelected: LocalDate? = null,
    val currentDisplayMonth: LocalDate = LocalDate.now().withDayOfMonth(1)
){
    val groupedOrders: Map<String, List<OrderUiModel>>
        get() = orders.data?.groupBy { it.displayHeader } ?: emptyMap()
}

data class OrderInternalState (
    val activeCategory: OrderFilterCategory = OrderFilterCategory.ALL,
    val isCalendarVisible: Boolean = false,
    val startDateSelected: LocalDate? = null,
    val endDateSelected: LocalDate? =null,
    val currentDisplayMonth: LocalDate = LocalDate.now().withDayOfMonth(1)
)

sealed class OrderEvent {
    data class NavigateToPayment(val orderId: String, val total: String) : OrderEvent()
    data class NavigateToDetail(val orderId: String) : OrderEvent()
}