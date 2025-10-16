package com.example.core.data.model

data class MenuDto(
    val id: String,
    val name: String,
    val description: String,
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
    val restaurantId: String
)
