package com.example.core.ui.mapper

import com.example.core.domain.model.AddressType
import com.example.core.domain.model.LocationDetails
import com.example.core.ui.model.UserAddressUiModel

fun LocationDetails?.toUiModel(): UserAddressUiModel{
    return UserAddressUiModel(
        id = "id",
        fullAddress = "",
        latitude = this?.latitude?:0.0,
        longitude = this?.longitude?:0.0,
        addressName = "",
        landmarkDetail = "",
        addressType = AddressType.NONE
    )
}