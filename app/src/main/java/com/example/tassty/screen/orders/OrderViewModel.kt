package com.example.tassty.screen.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserOrderUseCase
import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import com.example.core.utils.toImmutableListState
import com.example.tassty.util.OrderPreviewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getUserOrderUseCase: GetUserOrderUseCase
) : ViewModel() {

    private val _navigation = MutableSharedFlow<OrderEvent>()
    val navigation = _navigation.asSharedFlow()

    private val _internalState = MutableStateFlow(OrderInternalState())

    private val filteredOrderContent = combine(
        getUserOrderUseCase(),
        _internalState
    ) { resource, internal ->
        resource.toImmutableListState { orderDomain -> orderDomain.toUiModel() }.let { uiResource ->
            uiResource.copy(
                data = uiResource.data?.filter { order ->
                    val categoryMatch = when (internal.activeCategory) {
                        OrderFilterCategory.ALL -> true
                        OrderFilterCategory.ONGOING -> order.status in listOf(
                            OrderStatus.PLACED, OrderStatus.PREPARING, OrderStatus.ON_DELIVERY
                        )
                        OrderFilterCategory.COMPLETED -> order.status == OrderStatus.COMPLETED
                    }

                    val dateMatch = if (internal.startDateSelected != null && internal.endDateSelected != null) {
                        !order.orderDate.isBefore(internal.startDateSelected) &&
                                !order.orderDate.isAfter(internal.endDateSelected)
                    } else true

                    categoryMatch && dateMatch
                }?.toImmutableList()
            )
        }
    }

    val uiState = combine(
        filteredOrderContent,
        _internalState
    ) { orders, internal ->

        val groupedOrders: ImmutableMap<String, ImmutableList<OrderUiModel>> =
            orders.data?.groupBy { it.displayHeader }
                ?.mapValues { it.value.toImmutableList() }
                ?.toImmutableMap()
                ?: persistentMapOf()

        val updatedFilters = OrderPreviewData.orderFilters.map {
            it.copy(isSelected = it.category == internal.activeCategory)
        }

        OrderUiState(
            orders = orders,
            filter = updatedFilters,
            groupedOrders = groupedOrders,
            selectedCategory = internal.activeCategory,
            isCalendarVisible = internal.isCalendarVisible,
            startDateSelected = internal.startDateSelected,
            endDateSelected = internal.endDateSelected,
            currentDisplayMonth = internal.currentDisplayMonth
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OrderUiState()
    )


    fun onFilterSelected(category: OrderFilterCategory) {
        _internalState.update { it.copy(activeCategory=category) }
    }

    fun onCalendarClick(visible : Boolean) {
        _internalState.update { it.copy(isCalendarVisible = visible) }
    }

    fun onDateSelected(date: LocalDate) {
        _internalState.update { currentState ->
            when {
                currentState.startDateSelected == null || (currentState.endDateSelected != null) -> {
                    currentState.copy(
                        startDateSelected = date,
                        endDateSelected = null
                    )
                }
                // Jika tanggal yang diklik sebelum startDate, jadikan startDate yang baru
                date.isBefore(currentState.startDateSelected) -> {
                    currentState.copy(
                        startDateSelected = date,
                        endDateSelected = null
                    )
                }
                // Jika setelah startDate, maka ini adalah endDate
                else -> {
                    currentState.copy(
                        endDateSelected = date
                    )
                }
            }
        }
    }

    fun onMonthChange(newMonth: LocalDate) {
        _internalState.update {
            it.copy(currentDisplayMonth = newMonth)
        }
    }

    fun onCancelClick(){
        _internalState.update {
            it.copy(isCalendarVisible = false, startDateSelected = LocalDate.now())
        }
    }

    fun onOrderClicked(order: OrderUiModel) {
        viewModelScope.launch {
            if (order.status == OrderStatus.PENDING) {
                _navigation.emit(OrderEvent.NavigateToPayment(order.id,order.finalAmount))
            } else if(order.status == OrderStatus.COMPLETED){
                _navigation.emit(OrderEvent.NavigateToOrderDetail(order.id))
            } else{
                _navigation.emit(OrderEvent.NavigateToOrderProcess(order.id))
            }
        }
    }
}