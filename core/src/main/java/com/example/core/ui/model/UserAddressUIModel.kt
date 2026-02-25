package com.example.core.ui.model

import com.example.core.domain.model.AddressType

data class UserAddressUiModel (
    val id: String,
    val fullAddress: String,
    val addressName: String,
    val landmarkDetail: String,
    val latitude: Double,
    val longitude: Double,
    val addressType: AddressType = AddressType.NONE,
    val isPrimary: Boolean,
    val isSelected: Boolean
){
    val formatAddressType: String
        get() = if (addressType == AddressType.NONE) "Type" else addressType.displayName

    val formatAddressName : String
        get() = addressName.ifEmpty { "Address title" }

    val formatFullAddress: String
        get() = fullAddress.ifEmpty { "-" }
}