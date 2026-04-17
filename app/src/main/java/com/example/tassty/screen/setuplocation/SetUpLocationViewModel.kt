package com.example.tassty.screen.setuplocation

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AddressType
import com.example.core.domain.model.UserAddress
import com.example.core.domain.usecase.GetAddressCoordinateUseCase
import com.example.core.domain.usecase.SetupUserAccountUseCase
import com.example.core.domain.usecase.GetCurrentLocationUseCase
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.UserUiModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetUpLocationViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val setupUserAccountUseCase: SetupUserAccountUseCase,
    private val getAddressCoordinateUseCase: GetAddressCoordinateUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(SetUpLocationInternalState())

    private val _events = MutableSharedFlow<SetUpLocationEvent>()
    val events = _events.asSharedFlow()

    val uiState : StateFlow<SetUpLocationUiState> = _internalState.map {  state ->
        SetUpLocationUiState(
            userAddress = state.userAddress,
            isLoading = state.isLoading,
            isModalVisible = state.isModalVisible,
            selectedLatLng = state.selectedLatLng,
            tempLandmark = state.tempLandmark,
            tempAddressName = state.tempAddressName,
            tempAddressType = state.tempAddressType,
            tempFullAddress = state.tempFullAddress,
            isButtonEnabled = state.isFormComplete
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SetUpLocationUiState()
    )

    init {
        fetchInitialLocation()
    }
    private fun fetchInitialLocation() {
        viewModelScope.launch {
            _internalState.update { it.copy(isLoading = true) }

            val location = getCurrentLocationUseCase()

            _internalState.update { state ->
                state.copy(
                    isLoading = false,
                    selectedLatLng = LatLng(location.latitude, location.longitude),
                    tempFullAddress = location.fullAddress,
                    tempAddressType = AddressType.PERSONAL
                )
            }
        }
    }

    fun onSetLocationClick(visible: Boolean){
        _internalState.update { it.copy(isModalVisible = visible) }
    }

    fun onAddressNameChange(name: String) {
        _internalState.update { it.copy(tempAddressName = name) }
    }

    fun onLandmarkDetailChange(landmark: String) {
        _internalState.update { it.copy(tempLandmark = landmark) }
    }

    fun onTypeSelected(type: AddressType) {
        _internalState.update { it.copy(tempAddressType = type) }
    }

    fun onMapClicked(latLng: LatLng) {
        viewModelScope.launch {
            val fullAddress = getAddressCoordinateUseCase(latLng.latitude, latLng.longitude)

            _internalState.update {
                it.copy(
                    selectedLatLng = latLng,
                    tempFullAddress = fullAddress,
                )
            }
        }
    }

    fun onSaveLocation() {
        _internalState.update { state ->
            state.copy(
                userAddress = UserAddressUiModel(
                    id = "",
                    latitude = state.selectedLatLng?.latitude ?: 0.0,
                    longitude = state.selectedLatLng?.longitude ?: 0.0,
                    location = state.selectedLatLng?: LatLng(0.0,0.0),
                    addressName = state.tempAddressName,
                    landmarkDetail = state.tempLandmark,
                    fullAddress = state.tempFullAddress,
                    addressType = state.tempAddressType,
                    isSelected = false,
                    isPrimary = true,
                    isSwipeActionVisible = false
                ),
                isModalVisible = false
            )
        }
    }

    fun onSubmitAddress(cuisines: List<String>){
        val domainAddress = uiState.value.userAddress?.toDomain()?: return
        viewModelScope.launch {
            setupUserAccountUseCase(domainAddress,cuisines).collect { result->
                when(result) {
                    is TasstyResponse.Error -> {
                        _internalState.update { it.copy(isLoading = false, errorMessage = result.meta.message) }
                        _events.emit(SetUpLocationEvent.ShowToast(result.meta.message))
                    }
                    is TasstyResponse.Loading -> _internalState.update { it.copy(isLoading = true) }
                    is TasstyResponse.Success -> {
                        _internalState.update { it.copy(isLoading = false) }
                        _events.emit(SetUpLocationEvent.OnNavigateToComplete)
                    }
                }
            }
        }
    }
}