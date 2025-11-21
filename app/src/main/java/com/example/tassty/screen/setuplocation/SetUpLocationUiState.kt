package com.example.tassty.screen.setuplocation

import com.example.core.domain.model.AddressType
import com.example.core.ui.model.UserAddressUiModel
import com.google.android.gms.maps.model.LatLng

data class SetUpLocationUiState(
    val userAddress: UserAddressUiModel = UserAddressUiModel(),
    val isModalVisible: Boolean = false,
    val tempAddressName: String = "",
    val tempLandmark: String = "",
    val tempFullAddress: String = "",
    val tempAddressType: AddressType = AddressType.NONE,
    val selectedLatLng: LatLng? = null,
    val isLoading: Boolean = false,
    val errorMessage : String? = null
)

sealed class SetUpLocationEvent{
    object OnNavigateToComplete : SetUpLocationEvent()
    data class ShowToast(val message: String) : SetUpLocationEvent()
}
