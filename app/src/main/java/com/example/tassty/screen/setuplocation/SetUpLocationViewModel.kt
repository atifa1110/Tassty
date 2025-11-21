package com.example.tassty.screen.setuplocation

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AddressType
import com.example.core.domain.usecase.AddUserAddressUseCase
import com.example.core.domain.usecase.GetCurrentLocationUseCase
import com.example.core.ui.mapper.toRequestDto
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SetUpLocationViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val addUserAddressUseCase: AddUserAddressUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(SetUpLocationUiState())
    val uiState: StateFlow<SetUpLocationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SetUpLocationEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            getCurrentLocationUseCase().collect { result ->
                when(result){
                    is TasstyResponse.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = result.meta.message)
                        }
                        _events.emit(SetUpLocationEvent.ShowToast(result.meta.message))
                    }
                    is TasstyResponse.Loading -> _uiState.update {
                        it.copy(isLoading = true)
                    }
                    is TasstyResponse.Success -> _uiState.update {
                        it.copy(isLoading = false,
                        userAddress = result.data.toUiModel())
                    }
                }
            }
        }
    }

    fun onSetLocationClick(visible: Boolean){
        _uiState.update { it.copy(isModalVisible = visible) }
    }

    fun onAddressNameChange(name: String) {
        _uiState.update { it.copy(tempAddressName = name) }
    }

    fun onLandmarkDetailChange(landmark: String) {
        _uiState.update { it.copy(tempLandmark = landmark) }
    }

    fun onTypeSelected(type: AddressType) {
        _uiState.update { it.copy(tempAddressType = type) }
    }

    fun onMapClicked(context: Context, latLng: LatLng) {
        _uiState.update { it.copy(selectedLatLng = latLng) }

        viewModelScope.launch(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val fullAddress = result?.firstOrNull()?.getAddressLine(0) ?: ""

            _uiState.update { it.copy(tempFullAddress = fullAddress) }
        }
    }

    fun onSaveLocation() {
        _uiState.update { state ->
            state.copy(
                isModalVisible = false,
                userAddress = UserAddressUiModel(
                    fullAddress = state.tempFullAddress,
                    addressName = state.tempAddressName,
                    landmarkDetail = state.tempLandmark,
                    addressType = state.tempAddressType,
                    longitude = state.selectedLatLng?.longitude?:0.0,
                    latitude = state.selectedLatLng?.latitude?:0.0
                )
            )
        }
    }

    fun onSubmitAddress(cuisines: List<String>){
        val data = uiState.value.userAddress.toRequestDto(cuisines)
        viewModelScope.launch {
            addUserAddressUseCase.invoke(data).collect { result->
                when(result) {
                    is TasstyResponse.Error -> _uiState.update { it.copy(isLoading = false) }
                    is TasstyResponse.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is TasstyResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _events.emit(SetUpLocationEvent.OnNavigateToComplete)
                    }
                }
            }
        }
    }


}