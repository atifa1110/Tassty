package com.example.core.ui.model

import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantStatus
import kotlin.collections.joinToString

// UI Model
data class RestaurantUiModel(
    val restaurant: Restaurant,
    val distance: Int?,
    val operationalStatus: RestaurantStatus
){
    val formattedCategories: String
        get() = restaurant.category.joinToString(separator = ", ")

    val formattedDistance: String
        get() = distance?.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        } ?: "N/A"

    val lat : Double get() =  restaurant.locationDetails.latitude
    val long : Double get() = restaurant.locationDetails.longitude
}

//enum class RestaurantStatus {
//    OPEN,
//    CLOSED,
//    OFFDAY
//}

data class RestaurantStatusResult(
    val status: RestaurantStatus,
    val message: String
)
