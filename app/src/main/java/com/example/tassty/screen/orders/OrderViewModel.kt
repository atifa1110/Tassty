package com.example.tassty.screen.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetUserOrderUseCase
import com.example.core.domain.utils.toListState
import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import com.example.tassty.orderFilters
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val orderContent = getUserOrderUseCase().map { it.toListState { it.toUiModel() } }

    val uiState = combine(
        orderContent,
        _internalState
    ) { orders, internal ->


        val filteredData = orders.copy(
            data = orders.data?.filter { order ->
                val categoryMatch = when (internal.activeCategory) {
                    OrderFilterCategory.ALL -> true
                    OrderFilterCategory.ONGOING ->
                        order.status in listOf(OrderStatus.PLACED, OrderStatus.PREPARING,
                            OrderStatus.ON_DELIVERY)
                    OrderFilterCategory.COMPLETED ->
                        order.status == OrderStatus.COMPLETED
                }
                val dateMatch = if (
                    internal.startDateSelected != null &&
                    internal.endDateSelected != null
                ) {
                    order.orderDate in internal.startDateSelected..internal.endDateSelected
                } else {
                    true
                }

                categoryMatch && dateMatch
            }
        )

        val updatedFilters = orderFilters.map {
            it.copy(isSelected = it.category == internal.activeCategory)
        }

        OrderUiState(
            orders = filteredData,
            filter = updatedFilters,
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
                // Jika belum ada start, atau sudah ada keduanya (reset), set sebagai start baru
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
            } else {
                _navigation.emit(OrderEvent.NavigateToDetail(order.id))
            }
        }
    }
}