package com.example.core.domain.model

data class CartGroup(
    val restaurant : Restaurant,
    val menus : List<CartItem>
){
    val totalPrice: Int get() = menus.sumOf { it.price * it.quantity }
    val totalQuantity: Int get() = menus.sumOf { it.quantity }
}

data class CartItem(
    val cartId: String,
    val menuId: String,
    val name: String,
    val imageUrl: String,
    val customizable: Boolean,
    val price: Int,
    val quantity: Int,
    val options: String,
    val optionIds: List<String>,
    val notes: String
)