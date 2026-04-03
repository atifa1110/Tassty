package com.example.tassty.screen.detailorder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.GetDetailOrderUseCase
import com.example.core.ui.utils.mapToResource
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.OrderItemUiModel
import com.example.tassty.RatingType
import com.example.tassty.navigation.OrderDetailDestination
import com.example.tassty.navigation.RatingNavArg
import com.example.tassty.screen.detailmenu.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailOrderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailOrderUseCase: GetDetailOrderUseCase
) : ViewModel() {

    val orderId = OrderDetailDestination.getId(savedStateHandle)

    private val _internalState = MutableStateFlow(DetailOrderInternalState())

    private val _uiEffect = Channel<DetailOrderEvent>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    val uiState: StateFlow<DetailOrderUiState> = combine(
        getDetailOrderUseCase(orderId),
        _internalState
    ) { resource, internal ->
        DetailOrderUiState(
            detail = resource.mapToResource { it.toUiModel() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailOrderUiState()
    )

    fun onRatingMenuClick(orderItem : OrderItemUiModel) = viewModelScope.launch {
        val state = uiState.value.detail.data?: return@launch
        val data = RatingNavArg(
            orderId = state.id,
            orderNumber = state.orderNumber,
            createdAt = state.createdAt,
            menuId = orderItem.id,
            imageUrl = orderItem.imageUrl,
            name = orderItem.menuName,
            ratingType = RatingType.MENU
        )
        _uiEffect.send(DetailOrderEvent.NavigateToRating(data))
    }

    fun onRatingRestClick() = viewModelScope.launch {
        val state = uiState.value.detail.data?: return@launch
        val data = RatingNavArg(
            orderId = state.id,
            orderNumber = state.orderNumber,
            createdAt = state.createdAt,
            menuId = state.restaurant.id,
            imageUrl = state.restaurant.imageUrl,
            name = state.restaurant.name,
            ratingType = RatingType.RESTAURANT
        )
        _uiEffect.send(DetailOrderEvent.NavigateToRating(data))
    }
}