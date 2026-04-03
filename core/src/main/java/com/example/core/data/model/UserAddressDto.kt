package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class UserAddressDto(
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("full_address")
    val fullAddress: String,
    @SerializedName("address_name")
    val addressName: String? = "",
    @SerializedName("landmark_detail")
    val landmarkDetail: String? = "",
    @SerializedName("address_type")
    val addressType: String? = null,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("is_primary")
    val isPrimary: Boolean = false
)

