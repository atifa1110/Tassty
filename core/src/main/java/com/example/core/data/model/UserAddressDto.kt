package com.example.core.data.model

data class UserAddressDto(
    val id: String,
    val fullStreetAddress: String,
    val latitude: Double,
    val longitude: Double,
    val addressName: String,
    val landmarkDetail: String,
    val addressType: String,
)

