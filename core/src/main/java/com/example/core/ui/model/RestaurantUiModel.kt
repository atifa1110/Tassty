package com.example.core.ui.model

import com.example.core.domain.model.LocationDetail
import com.google.android.gms.maps.model.LatLng

// UI Model
data class RestaurantUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val location : LatLng,
    val rank: Int,
    val rating: Double,
    val totalReviews: Int,
    val deliveryCost: Int,
    val deliveryTime: String,
    val distance: Int,
    val categories: String,
    val locationDetail: LocationDetail,
    val statusResult: RestaurantStatusResult,
) {

    val formatRating: String
        get() = rating.toString()

    val formatReviewCount: String
        get() = if(totalReviews <= 200) "$totalReviews" else "(200+)"

    val formattedDistance: String
        get() = distance.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        }
}
