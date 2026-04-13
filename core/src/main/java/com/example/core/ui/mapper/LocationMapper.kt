package com.example.core.ui.mapper

import com.example.core.domain.model.AddressType
import com.example.core.domain.model.LocationDetail
import com.example.core.ui.model.UserAddressUiModel
import com.google.android.gms.maps.model.LatLng

fun LocationDetail?.toUiModel(): UserAddressUiModel{
    return UserAddressUiModel(
        id = "id",
        fullAddress = "",
        latitude = this?.latitude?:0.0,
        longitude = this?.longitude?:0.0,
        location = LatLng(this?.latitude?:0.0,this?.longitude?:0.0),
        addressName = "",
        landmarkDetail = "",
        addressType = AddressType.NONE,
        isPrimary = false,
        isSelected = false
    )
}