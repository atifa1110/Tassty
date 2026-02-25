package com.example.core.ui.mapper

import com.example.core.data.model.UserAddressDto
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.domain.model.AddressType
import com.example.core.domain.model.UserAddress
import com.example.core.ui.model.UserAddressUiModel

fun UserAddress.toRequestDto(cuisines: List<String>): SetUpRequest{
    return SetUpRequest(
        addressName = this.addressName,
        fullAddress = this.fullAddress,
        latitude = this.latitude,
        longitude = this.longitude,
        landmarkDetail = this.landmarkDetail,
        addressType = this.addressType.name,
        categoryIds = cuisines
    )
}

fun UserAddress.toUiModel() = UserAddressUiModel(
    id = this.id,
    fullAddress = this.fullAddress,
    addressName = this.addressName,
    landmarkDetail =  this.landmarkDetail,
    latitude = this.latitude,
    longitude = this.longitude,
    addressType = this.addressType,
    isPrimary = this.isPrimary,
    isSelected = false
)

fun UserAddressUiModel.toDomain(): UserAddress{
    return UserAddress(
        id = this.id,
        addressName = this.addressName,
        fullAddress = this.fullAddress,
        latitude = this.latitude,
        longitude = this.longitude,
        landmarkDetail = this.landmarkDetail,
        addressType = this.addressType,
        isPrimary = this.isPrimary
    )
}
