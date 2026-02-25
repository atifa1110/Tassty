package com.example.core.ui.model

import com.example.core.domain.model.RestaurantStatus

// UI Model
data class DetailRestaurantUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val city: String,
    val fullAddress: String,
    val categories: String,
    val rank: Int,
    val rating: Double,
    val totalReviews: Int,
    val deliveryTime: String,
    val distance: Int,
    val isVerified: Boolean,
    val operationalDay: List<OperationalDayUi>,
    val deliveryCost: Int,
    val statusResult: RestaurantStatusResult,
    val isWishlist: Boolean
){
    val todayHour = operationalDay.firstOrNull { it.isToday }?.hours

    val formatDistance: String
        get() = distance.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        }

    val formatRating: String
        get() = rating.toString()

    val formatReviewCount: String
        get() = if(totalReviews <= 200) "$totalReviews" else "(200+)"
}

data class OperationalDayUi(
    val day: String,
    val hours: String,
    val isToday: Boolean
)

data class RestaurantStatusResult(
    val status: RestaurantStatus,
    val message: String
)