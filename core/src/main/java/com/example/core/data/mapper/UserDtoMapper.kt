package com.example.core.data.mapper

import com.example.core.data.model.AuthDto
import com.example.core.data.model.UserAddressDto
import com.example.core.data.model.UserDto
import com.example.core.domain.model.AddressType
import com.example.core.domain.model.AuthUser
import com.example.core.domain.model.User
import com.example.core.domain.model.UserAddress

fun AuthDto.toDomain() : AuthUser {
    return AuthUser(
        accessToken = this.accessToken?:"",
        refreshToken = this.refreshToken?:"",
        steamToken = this.steamToken?:"",
        name = this.name?:"",
        profileImage = this.profileImage?:"",
        addressName = this.addressName?:""
    )
}

fun UserAddressDto.toDomain() = UserAddress(
    id = this.id,
    fullAddress = this.fullAddress,
    addressName = this.addressName,
    landmarkDetail =  this.landmarkDetail,
    latitude = this.latitude,
    longitude = this.longitude,
    addressType = AddressType.valueOf(this.addressType),
    isPrimary = this.isPrimary
)

fun UserDto.toDomain() = User(
    id = this.id,
    email = this.email,
    name = this.name,
    profileImage = this.profileImage,
    categoryIds = this.categoryIds
)
