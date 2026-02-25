package com.example.core.ui.mapper


import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.CartItem
import com.example.core.ui.model.CartGroupUiModel
import com.example.core.ui.model.CartItemUiModel

fun CartGroup.toUiModel(): CartGroupUiModel{
    return CartGroupUiModel(
        restaurant = this.restaurant.toUiModel(),
        menus = this.menus.map { it.toUiModel() }
    )
}

fun CartItem.toUiModel(): CartItemUiModel{
    return CartItemUiModel(
        cartId = this.cartId,
        menuId = this.menuId,
        name = this.name,
        imageUrl = this.imageUrl,
        price = this.price,
        quantity = this.quantity,
        summary = this.summary,
        notes = notes,
        isSelected = false,
        isSwipeActionVisible = false
    )
}