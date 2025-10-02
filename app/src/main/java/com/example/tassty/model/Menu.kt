package com.example.tassty.model

data class Menu(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val rating: Double,
    val sold: Int,
    val distance: Int,
    val imageUrl: String,
    val isWishlist: Boolean = false
)