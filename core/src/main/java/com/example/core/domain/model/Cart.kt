package com.example.core.domain.model

data class Cart (
    val cartId: String,
    val menuId: String,
    val restaurantId: String,
    val quantity: Int,
    val finalSummary: String,
    val notes: String
)