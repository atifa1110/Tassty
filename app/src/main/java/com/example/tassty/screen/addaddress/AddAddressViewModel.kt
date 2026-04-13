package com.example.tassty.screen.addaddress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AddressType
import com.example.core.domain.usecase.CreateUserAddressUseCase
import com.example.core.domain.usecase.GetAddressCoordinateUseCase
import com.example.core.domain.usecase.GetCurrentLocationUseCase
import com.example.tassty.screen.addcard.AddCardUiEffect
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val createUserAddressUseCase: CreateUserAddressUseCase,
    private val getAddressCoordinateUseCase: GetAddressCoordinateUseCase
) : ViewModel() {

    private val _internalState = MutableStateFlow(AddAddressInternalState())

    private val _uiEffect = Channel<AddAddressUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    val uiState = _internalState.map {  internalState ->
        AddAddressUiState(
            addressName = internalState.addressName,
            fullAddress = internalState.fullAddress,
            landmark = internalState.landmark,
            selectedLatLng = internalState.selectedLatLng,
            addressType = internalState.addressType,
            isLoading = internalState.isLoading,
            isMapLoading = internalState.isMapLoading,
            isButtonEnabled = internalState.isButtonEnabled
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AddAddressUiState()
    )

    init {
        fetchInitialLocation()
    }

    private fun fetchInitialLocation() {
        viewModelScope.launch {
            _internalState.update { it.copy(isMapLoading = true) }

            val location = getCurrentLocationUseCase()

            _internalState.update { state ->
                state.copy(
                    isMapLoading = false,
                    selectedLatLng = LatLng(location.latitude, location.longitude),
                    fullAddress = location.fullAddress,
                )
            }
        }
    }

    private val _cameraEvents = Channel<LatLng>(Channel.BUFFERED)
    val cameraEvents = _cameraEvents.receiveAsFlow()

    fun onMapClicked(latLng: LatLng) {
        viewModelScope.launch {
            val fullAddress = getAddressCoordinateUseCase(latLng.latitude, latLng.longitude)

            _internalState.update {
                it.copy(
                    selectedLatLng = latLng,
                    fullAddress = fullAddress,
                )
            }
            _cameraEvents.send(latLng)
        }
    }

    fun onAddressNameChange(name: String) {
        _internalState.update { it.copy(addressName = name, isButtonEnabled = true) }
    }

    fun onLandmarkDetailChange(landmark: String) {
        _internalState.update { it.copy(landmark = landmark) }
    }

    fun onTypeSelected(type: AddressType) {
        _internalState.update { it.copy(addressType = type) }
    }

    fun onCreateAddress() = viewModelScope.launch {
        val state = _internalState.value
        createUserAddressUseCase(
            addressName = state.addressName,
            fullAddress = state.fullAddress,
            landmarkDetail = state.landmark,
            latitude = state.selectedLatLng?.latitude?:0.0,
            longitude = state.selectedLatLng?.longitude?:0.0,
            addressType = state.addressType.name
        ).collect { response ->
            when(response){
                is TasstyResponse.Error -> {
                    _internalState.update { it.copy(isLoading = false) }
                    _uiEffect.send(AddAddressUiEffect.ShowMessage(response.meta.message))
                }
                is TasstyResponse.Loading -> _internalState.update { it.copy(isLoading = true) }
                is TasstyResponse.Success -> {
                    _internalState.update { it.copy(isLoading = false) }
                    _uiEffect.send(AddAddressUiEffect.NavigateBack)
                }
            }
        }
    }

}