package com.example.core.domain.model

data class UserAddress(
    val id: String,
    val fullAddress: String,
    val addressName: String,
    val landmarkDetail: String,
    val latitude: Double,
    val longitude: Double,
    val addressType: AddressType,
    val isPrimary: Boolean
)


enum class AddressType(val displayName: String) {
    NONE(""),
    PERSONAL("Personal"),
    BUSINESS("Business")
}