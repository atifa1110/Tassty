package com.example.core.data.model

import com.google.gson.annotations.SerializedName

// DTO pendukung tidak perlu diubah
data class OperationalDayDto(
    val day: String,
    val hours: String
)

data class RestaurantDto(
    val id: String = "",
    val name: String = "",
    val imageUrl: String? = null,
    val category: List<String> = emptyList(),
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val deliveryTime: String = "",
    val rank: Int? = null,
    val fullAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = "",
    val postcode: String? = null,
    val operationalHours: List<OperationalDayDto> = emptyList()
)

