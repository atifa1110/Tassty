package com.example.tassty.screen.orderprocess

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.CreateChatChannelUseCase
import com.example.core.domain.usecase.GetDetailOrderRouteUseCase
import com.example.core.domain.usecase.GetDetailOrderUseCase
import com.example.core.domain.usecase.TrackDriverLocationUseCase
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.mapper.toUiOrderModel
import com.example.core.ui.model.OrderStatus
import com.example.core.utils.mapToResource
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
    private val createChatChannelUseCase: CreateChatChannelUseCase,
    private val getDetailOrderRouteUseCase: GetDetailOrderRouteUseCase,
    private val trackDriverLocationUseCase: TrackDriverLocationUseCase
) : ViewModel() {

    val orderId = OrderProcessDestination.getId(savedStateHandle)

    private val _internalState = MutableStateFlow(OrderProcessInternalUiState())
    private val _events = MutableSharedFlow<OrderProcessEvent>()
    val events: SharedFlow<OrderProcessEvent> = _events.asSharedFlow()

    private var isTrackingStarted = false
    private var trackingJob: kotlinx.coroutines.Job? = null

    val uiState: StateFlow<OrderProcessUiState> = combine(
        getDetailOrderUseCase(orderId),
        getDetailOrderRouteUseCase(orderId),
        trackDriverLocationUseCase(orderId),
        _internalState
    ) { detailResource, routeResource, track, internal ->

        val detailUi = detailResource.mapToResource { it.toUiModel() }
        val baseRouteUi = routeResource.mapToResource { it.toUiOrderModel() }
        val allPoints = baseRouteUi.data?.polylinePoints ?: emptyList()

        val isDelivery = detailUi.data?.status == OrderStatus.ON_DELIVERY

        val currentIndex = if (isDelivery) track.currentStepIndex else null

        val driverPos = if (isDelivery) {
            if (currentIndex != null && allPoints.isNotEmpty()) {
                allPoints.getOrNull(currentIndex) ?: detailUi.data?.restaurant?.location
            } else {
                detailUi.data?.restaurant?.location
            }
        } else {
            null
        }

        if ((detailUi.data?.status == OrderStatus.COMPLETED)
            && !internal.isArrivedModalVisible) {
            showArrivedModal()
        }

        OrderProcessUiState(
            detail = detailUi,
            route = baseRouteUi,
            driverPosition = driverPos,
            isArrivedModalVisible = internal.isArrivedModalVisible,
            currentStepIndex = currentIndex,
            isSimulated = track.isSimulated
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OrderProcessUiState()
    )

//    init {
//        viewModelScope.launch {
//            getDetailOrderUseCase(orderId).collect { resource ->
//                if (resource is TasstyResponse.Success) {
//                    val status = resource.data?.toUiModel()
//                    if (status?.status == OrderStatus.ON_DELIVERY) {
//                        startRealtimeTracking()
//                    }
//                }
//            }
//        }
//    }

    fun startRealtimeTracking() {
        if (isTrackingStarted) return

        isTrackingStarted = true
        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            trackDriverLocationUseCase(orderId).collect { payload ->
                Log.d("OrderProcessViewModel",payload.toString())
                _internalState.update {
                    it.copy(
                        currentStepIndex = payload.currentStepIndex,
                        isSimulated = payload.isSimulated
                    )
                }
            }
        }
    }

    private fun showArrivedModal() {
        viewModelScope.launch {
            delay(3000)
            _internalState.update { it.copy(isArrivedModalVisible = true) }
        }
    }

    fun onDismissModal() = viewModelScope.launch {
        _events.emit(OrderProcessEvent.NavigateBack)
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

    override fun onCleared() {
        super.onCleared()
        isTrackingStarted = false
        trackingJob?.cancel()
    }
}