package com.example.core.ui.mapper


import com.example.core.data.source.remote.request.OrderItemRequest
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
    val combinedResult = listOfNotNull(
        this.options.takeIf { it.isNotBlank() },
        this.notes.takeIf { it.isNotBlank() }?.let { "Notes: $it" }
    ).joinToString(separator = "\n")

    val finalFormat = combinedResult.ifEmpty { "Notes: -" }
    return CartItemUiModel(
        cartId = this.cartId,
        menuId = this.menuId,
        name = this.name,
        imageUrl = this.imageUrl,
        customizable = this.customizable,
        price = this.price,
        quantity = this.quantity,
        options = this.options,
        notes = this.notes,
        formatOptions = finalFormat,
        isSelected = false,
        isSwipeActionVisible = false
    )
}

fun CartItemUiModel.toRequest() : OrderItemRequest{
    return OrderItemRequest(
        menuId = this.menuId,
        quantity = this.quantity,
        price = this.price,
        options = this.options,
        notes = this.notes
    )
}