package com.example.tassty.screen.setuplocation

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AddressType
import com.example.core.domain.usecase.SetupUserAccountUseCase
import com.example.core.domain.usecase.GetCurrentLocationUseCase
import com.example.core.ui.mapper.toDomain
import com.example.core.ui.model.UserAddressUiModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SetUpLocationViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val setupUserAccountUseCase: SetupUserAccountUseCase
) : ViewModel(){

    private val _internalState = MutableStateFlow(SetUpLocationInternalState())

    private val _events = MutableSharedFlow<SetUpLocationEvent>()
    val events = _events.asSharedFlow()

    val uiState : StateFlow<SetUpLocationUiState> = combine(
        getCurrentLocationUseCase(),
        _internalState
    ) { location, inputs ->
        val finalLat = inputs.selectedLatLng?.latitude ?: location.latitude
        val finalLng = inputs.selectedLatLng?.longitude ?: location.longitude

        SetUpLocationUiState(
            userAddress = UserAddressUiModel(
                id = "",
                latitude = finalLat,
                longitude = finalLng,
                addressName = inputs.tempAddressName,
                landmarkDetail = inputs.tempLandmark,
                fullAddress = inputs.tempFullAddress,
                addressType = inputs.tempAddressType,
                isSelected = false,
                isPrimary = true
            ),
            isLoading = inputs.isLoading,
            isModalVisible = inputs.isModalVisible,
            selectedLatLng = inputs.selectedLatLng,
            tempLandmark = inputs.tempLandmark,
            tempAddressName = inputs.tempAddressName,
            tempAddressType = inputs.tempAddressType,
            tempFullAddress = inputs.tempFullAddress,
            buttonEnable =inputs.tempAddressName.isNotEmpty() &&
                    inputs.tempFullAddress.isNotEmpty() &&
                    inputs.tempAddressType!= AddressType.NONE &&
                    inputs.tempLandmark.isNotEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SetUpLocationUiState()
    )

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

    fun onMapClicked(context: Context, latLng: LatLng) {
        _internalState.update { it.copy(selectedLatLng = latLng) }

        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val fullAddress = result?.firstOrNull()?.getAddressLine(0) ?: ""

            _internalState.update { it.copy(tempFullAddress = fullAddress) }
        }
    }

    fun onSaveLocation() {
        _internalState.update { state ->
            state.copy(
                isModalVisible = false
            )
        }
    }

    fun onSubmitAddress(cuisines: List<String>){
        val domainAddress = uiState.value.userAddress.toDomain()
        Log.d("SetUpLocationViewModel",domainAddress.toString())
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