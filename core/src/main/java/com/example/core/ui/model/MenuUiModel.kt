package com.example.core.ui.model

import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.utils.toCleanRupiahFormat

data class MenuUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: Int,
    val soldCount: Int,
    val rank: Int,
    val customizable: Boolean,
    val isAvailable: Boolean,
    val maxQuantity: Int,
    val menuStatus: MenuStatus,
    val restaurant: RestaurantUiModel,
    val isWishlist : Boolean
){
    val formatPrice : String get() = price.toCleanRupiahFormat()
    val formatRating: String get() = restaurant.rating.toString()
    val formatSoldCount : String get() = "${soldCount}x"
    val formattedDistance: String
        get() = restaurant.distance.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        }
}
