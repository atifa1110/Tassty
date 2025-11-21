package com.example.core.ui.mapper

import com.example.core.data.source.remote.request.UserAddressRequest
import com.example.core.data.source.remote.request.UserSetUpRequest
import com.example.core.ui.model.UserAddressUiModel

fun UserAddressUiModel.toRequestDto(cuisines: List<String>): UserSetUpRequest{
    return UserSetUpRequest(
        address = UserAddressRequest(
            fullStreetAddress = this.fullAddress,
            latitude = this.latitude,
            longitude = this.longitude,
            addressName = this.addressName,
            landmarkDetail = this.landmarkDetail,
            addressType = this.addressType.displayName
        ),
        selectedMenu = cuisines
    )
}