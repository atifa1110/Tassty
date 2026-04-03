package com.example.core.data.source.local.mapper

import com.example.core.data.mapper.dummyRestaurant
import com.example.core.data.source.local.database.entity.CartEntity
import com.example.core.data.source.local.database.model.CartWithMenuAndRestaurant
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.CartItem

fun List<CartWithMenuAndRestaurant>.toSingleCartDomain(): CartGroup {
    if (this.isEmpty()) {
        return CartGroup(
            restaurant = dummyRestaurant,
            menus = emptyList()
        )
    }

    val firstItem = this.first()
    val menus = this.map {
        CartItem(
            cartId = it.cart.cartId,
            menuId = it.menu.id,
            name = it.menu.name,
            imageUrl = it.menu.imageUrl,
            customizable = it.menu.customizable,
            price = it.cart.price,
            quantity = it.cart.quantity,
            options = it.cart.finalSummary?:"",
            notes = it.cart.notes?:"",
        )
    }

    return CartGroup(
        restaurant = firstItem.restaurant.toDomainRestaurant(),
        menus = menus
    )
}

fun CartEntity.toDomain() = Cart(
    cartId = this.cartId,
    menuId = this.menuId,
    restaurantId = this.restaurantId,
    quantity = this.quantity,
    finalSummary = this.finalSummary?:"",
    notes = this.notes?:""
)