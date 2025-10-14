package com.example.core.domain.model

import com.example.core.data.model.OperationalDayDto
import com.example.core.ui.model.MenuStatus

// Domain Model
data class Menu (
    val id: String,
    val name: String,
    val imageUrl: String,
    val originalPrice: Int,
    val discountPrice: Int? = null,
    val isAvailable: Boolean,
    val rating: Double? = null,
    val soldCount: Int? = null,
    val isBestSeller: Boolean = false,
    val isRecommended: Boolean = false,
    val rank: Int? = null,
    val distanceMeters: Int? = null,
    val maxOrderQuantity: Int? = null,
    val operationalHours: List<OperationalDayDto>
){
    val price : Int = discountPrice?: originalPrice
}

data class MenuBusinessInfo(
    val menu: Menu,
    val isWishlist: Boolean = false,
    val status: MenuStatus
)