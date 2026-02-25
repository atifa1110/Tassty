package com.example.core.domain.model

import com.example.core.ui.model.RestaurantStatusResult

// Domain Model
data class DetailRestaurant(
    val id: String,
    val name: String,
    val imageUrl: String,
    val categories: List<String>,
    val isVerified: Boolean,
    val city: String,
    val fullAddress: String,
    val longitude: Double,
    val latitude: Double,
    val rank: Int,
    val rating: Double,
    val totalReviews: Int,
    val distance: Int,
    val deliveryCost: Int,
    val deliveryTime: String,
    val operationalHours: List<OperationalDay>,
    val isOpen: Boolean,
    val closingTimeServer: String,
    val currentDay: String,
    val isWishlist : Boolean = false,
){
    val statusResult : RestaurantStatusResult =
        getRestaurantStatus(isOpen,closingTimeServer)
}

data class OperationalDay(
    val day: String,
    val hours: String
)