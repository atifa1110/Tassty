package com.example.core.domain.model

import com.example.core.ui.model.RestaurantStatus

// Domain Model
data class Restaurant(
    val id: String,
    val name: String,
    val imageUrl: String,
    val category: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTime: String,
    val rank: Int? = null,
    val locationDetails: LocationDetails,
    val operationalHours: List<OperationalDay>,
){
    val cityName: String
        get() = locationDetails.city
}

data class LocationDetails(
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val postcode: String? = null
)

data class OperationalDay(
    val day: String,
    val hours: String,
    val isToday: Boolean = false
)

data class RestaurantBusinessInfo(
    val restaurant: Restaurant,
    val distance: Int?,
    val status: RestaurantStatus
)
