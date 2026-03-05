package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class DriverDto(
    val id: String,
    val name: String,
    val rating: Double,
    @SerializedName("profile_image")
    val profileImage: String
)
