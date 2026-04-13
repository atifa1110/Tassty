package com.example.core.domain.model

import androidx.compose.ui.res.stringResource
import com.example.core.R

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