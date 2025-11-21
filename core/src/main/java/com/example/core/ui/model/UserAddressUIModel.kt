package com.example.core.ui.model

import com.example.core.domain.model.AddressType

data class UserAddressUiModel (
    val id: String = "",
    val fullAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val addressName: String = "",
    val landmarkDetail: String = "",
    val addressType: AddressType = AddressType.NONE
){
    val formatAddressType: String
        get() = if (addressType == AddressType.NONE) "Type" else addressType.displayName

    val formatAddressName : String
        get() = addressName.ifEmpty { "Address title" }

    val formatFullAddress: String
        get() = fullAddress.ifEmpty { "-" }
}