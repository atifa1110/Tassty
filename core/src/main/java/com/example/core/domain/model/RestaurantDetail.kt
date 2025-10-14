package com.example.core.domain.model

// Domain Model
data class RestaurantDetail(
    val restaurant: Restaurant,
    val isVerified: Boolean,
    val deliveryCost: String
){
    val cityName: String
        get() = restaurant.locationDetails.city
}
