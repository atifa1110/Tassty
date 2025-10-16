package com.example.core.ui.model

import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.utils.toCleanRupiahFormat

data class MenuUiModel(
    val menu: Menu,
    val status: MenuStatus,
    val isWishlist : Boolean = false
){
    val formatPrice : String get() = menu.price.toCleanRupiahFormat()
    val formatOriginalPrice : String get() = menu.price.toCleanRupiahFormat()
    val formatSoldCount : String get() =  "${menu.soldCount}x"
    val formattedDistance: String
        get() = menu.distanceMeters?.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        } ?: "N/A"

    val formatRating : Double
        get()= menu.rating?:0.0
}
