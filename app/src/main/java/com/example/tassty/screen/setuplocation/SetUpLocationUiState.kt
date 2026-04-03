package com.example.tassty.screen.setuplocation

import com.example.core.domain.model.AddressType
import com.example.core.ui.model.UserAddressUiModel
import com.google.android.gms.maps.model.LatLng

data class SetUpLocationUiState(
    val userAddress: UserAddressUiModel? = null,
    val isModalVisible: Boolean = false,
    val tempAddressName: String = "",
    val tempLandmark: String = "",
    val tempFullAddress: String = "Choose your location",
    val tempAddressType: AddressType = AddressType.NONE,
    val selectedLatLng: LatLng? = null,
    val isLoading: Boolean = false,
    val errorMessage : String? = null,
    val isButtonEnabled: Boolean = false
)

data class SetUpLocationInternalState(
    val userAddress: UserAddressUiModel? = null,
    val isModalVisible: Boolean = false,
    val tempAddressName: String = "",
    val tempLandmark: String = "",
    val tempFullAddress: String = "",
    val tempAddressType: AddressType = AddressType.NONE,
    val selectedLatLng: LatLng? = null,
    val isLoading: Boolean = false,
    val errorMessage : String? = null
){
    val isFormComplete: Boolean
        get() = tempAddressName.isNotEmpty() &&
                tempFullAddress.isNotEmpty() &&
                tempAddressType != AddressType.NONE &&
                tempLandmark.isNotEmpty()
}

sealed class SetUpLocationEvent{
    object OnNavigateToComplete : SetUpLocationEvent()
    data class ShowToast(val message: String) : SetUpLocationEvent()
}
