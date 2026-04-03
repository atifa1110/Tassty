package com.example.core.ui.mapper

import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.domain.model.UserAddress
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.domain.model.Driver
import com.example.core.domain.model.User
import com.example.core.ui.model.DriverUiModel
import com.example.core.ui.model.UserUiModel

fun User.toUiModel(): UserUiModel {
    return UserUiModel(
        id = this.id,
        email = this.email,
        name = this.name,
        profileImage = this.profileImage,
        categoryIds = this.categoryIds.map { it.toUiModel() }
    )
}

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


fun Driver.toUiModel(): DriverUiModel {
    return DriverUiModel(
        id = this.id,
        name = this.name,
        rating = this.rating,
        profileImage = this.profileImage
    )
}