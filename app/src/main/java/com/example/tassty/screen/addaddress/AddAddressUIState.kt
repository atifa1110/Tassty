package com.example.tassty.screen.addaddress

import com.example.core.domain.model.AddressType
import com.google.android.gms.maps.model.LatLng

data class AddAddressUiState(
    val addressName: String = "",
    val landmark: String = "",
    val fullAddress: String = "Choose your location",
    val addressType: AddressType = AddressType.PERSONAL,
    val selectedLatLng: LatLng? = null,
    val isLoading: Boolean = false,
    val isMapLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)

data class AddAddressInternalState(
    val addressName: String = "",
    val landmark: String = "",
    val fullAddress: String = "Choose your location",
    val addressType: AddressType = AddressType.PERSONAL,
    val selectedLatLng: LatLng? = null,
    val isLoading: Boolean = false,
    val isMapLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)

sealed class AddAddressUiEffect {
    object NavigateBack : AddAddressUiEffect()
    data class ShowMessage(val message: String) : AddAddressUiEffect()
}