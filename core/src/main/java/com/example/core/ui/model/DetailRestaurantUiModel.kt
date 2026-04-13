package com.example.core.ui.model

import android.os.Parcelable
import com.example.core.domain.model.RestaurantStatus
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantLocationArgs(
    val id: String,
    val imageUrl: String,
    val name: String,
    val city: String,
    val fullAddress: String,
    val location: LatLng,
    val todayHour : String,
    val isVerified: Boolean,
    val rating: String,
    val totalReviews: String
) : Parcelable

data class DetailRestaurantUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val city: String,
    val fullAddress: String,
    val lat: Double,
    val lng: Double,
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
    val todayHour : String,
    val formatDistance: String,
    val formatRating: String,
    val formatReviewCount: String,
    val isWishlist: Boolean,
)

data class OperationalDayUi(
    val day: String,
    val hours: String,
    val isToday: Boolean
)

data class RestaurantStatusResult(
    val status: RestaurantStatus,
    val message: String
)