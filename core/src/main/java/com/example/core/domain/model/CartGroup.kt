package com.example.core.domain.model

data class CartGroup(
    val restaurant : Restaurant,
    val menus : List<CartItem>
)

data class CartItem(
    val cartId: String,
    val menuId: String,
    val name: String,
    val imageUrl: String,
    val customizable: Boolean,
    val price: Int,
    val quantity: Int,
    val options: String,
    val notes: String
)