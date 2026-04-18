package com.example.core.ui.model


data class CartGroupUiModel(
    val restaurant : RestaurantUiModel,
    val menus : List<CartItemUiModel>
)

data class CartItemUiModel(
    val cartId: String,
    val menuId: String,
    val name: String,
    val imageUrl: String,
    val customizable: Boolean,
    val price: Int,
    val quantity: Int,
    val options: String,
    val optionIds: List<String>,
    val notes: String,
    val formatOptions: String,
    val isSelected: Boolean,
    val isSwipeActionVisible: Boolean
)