package com.example.core.data.source.remote.request

data class UserAddressRequest(
    val addressName: String,
    val fullStreetAddress: String,
    val latitude: Double,
    val longitude: Double,
    val landmarkDetail: String,
    val addressType : String
)

data class UserSetUpRequest(
    val address: UserAddressRequest,
    val selectedMenu: List<String>
)
