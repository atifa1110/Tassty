package com.example.core.ui.model

import com.example.core.domain.model.Menu
import com.example.core.domain.utils.toCleanRupiahFormat

data class MenuUiModel(
    val menu: Menu,
    val status: MenuStatus,
    val isWishlist : Boolean = false
){
    val formatPrice : String get() = menu.price.toCleanRupiahFormat()
    val formatSoldCount : String get() =  "${menu.soldCount}x sold"
    val formattedDistance: String
        get() = menu.distanceMeters?.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        } ?: "N/A"
}

interface DisplayStatus {
    val isEnabled: Boolean
}

//enum class RestaurantStatus : DisplayStatus {
//    OPEN { override val isEnabled = true },
//    CLOSED { override val isEnabled = false },
//    OFFDAY { override val isEnabled = false }
//}

enum class MenuStatus : DisplayStatus {
    AVAILABLE { override val isEnabled = true },
    SOLDOUT { override val isEnabled = false },
    CLOSED { override val isEnabled = false }
}