package com.example.tassty.model

data class RestaurantDetail(
    val restaurant: Restaurant,
    val isVerified: Boolean,
    val deliveryCost: String
){
    val cityName: String
        get() = restaurant.locationDetails.city
}

// UI Model
data class RestaurantDetailUiModel(
    val detail: RestaurantDetail,
    val distance: Int?,
    val isWishlist: Boolean,
    val operationalStatus: RestaurantStatusResult
){
    val formattedCategories: String
        get() = detail.restaurant.category.joinToString(separator = ", ")

    val formattedDistance: String
        get() = distance?.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        } ?: "N/A"

    val deliveryCostText: String
        get() = detail.deliveryCost.ifEmpty{"Free"}

    val formattedReviewCount: String
        get() = if(detail.restaurant.reviewCount >= 200) "${detail.restaurant.reviewCount}" else "(200+)"
}
