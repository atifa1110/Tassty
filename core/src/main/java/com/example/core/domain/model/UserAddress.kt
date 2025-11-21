package com.example.core.domain.model

data class UserAddress(
    val id: String,
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val addressName: String,
    val landmarkDetail: String,
    val addressType: AddressType
)


enum class AddressType(val displayName: String) {
    NONE(""),
    PERSONAL("Personal"),
    BUSINESS("Business")
}