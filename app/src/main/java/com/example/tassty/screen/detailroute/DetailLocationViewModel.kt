package com.example.tassty.screen.detailroute

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.Resource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetCurrentLocationUseCase
import com.example.core.domain.usecase.GetDetailRestaurantRouteUseCase
import com.example.core.ui.mapper.toUiInfoModel
import com.example.tassty.navigation.DetailLocationDestination
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailLocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailRestaurantRouteUseCase: GetDetailRestaurantRouteUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    private val navData = DetailLocationDestination.getData(savedStateHandle)

    private val _cameraEvents = Channel<CameraUpdate>(Channel.CONFLATED)
    val cameraEvents = _cameraEvents.receiveAsFlow()


    val uiState: StateFlow<DetailLocationUiState> = getDetailRestaurantRouteUseCase(navData.id)
        .map { response ->
            val location = getCurrentLocationUseCase()
            val currentState = DetailLocationUiState(
                restaurant = navData,
            )
            when (response) {
                is TasstyResponse.Loading -> {
                    currentState.copy(route = Resource(isLoading = true))
                }
                is TasstyResponse.Success -> {
                    val routeUi = response.data?.toUiInfoModel()
                    currentState.copy(
                        route = Resource(data = routeUi, isLoading = false),
                        userLatLng = LatLng(location.latitude, location.longitude),
                        routeCenterPoint = routeUi?.polylinePoints?.let { points ->
                            if (points.isNotEmpty()) points[points.size / 2] else null
                        }
                    )
                }
                is TasstyResponse.Error -> {
                    currentState.copy(
                        route = Resource(
                            isLoading = false,
                            errorMessage = response.meta.message
                        )
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailLocationUiState(
                restaurant = navData
            )
        )

    fun onZoomIn() {
        _cameraEvents.trySend(CameraUpdateFactory.zoomIn())
    }

    fun onZoomOut() {
        _cameraEvents.trySend(CameraUpdateFactory.zoomOut())
    }

    fun updateCameraToMarkers(user: LatLng?, resto: LatLng) {
        user?.let {
            val bounds = LatLngBounds.builder()
                .include(it)
                .include(resto)
                .build()

            _cameraEvents.trySend(CameraUpdateFactory.newLatLngBounds(bounds, 450))
        }
    }
}