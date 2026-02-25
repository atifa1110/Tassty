package com.example.core.data.mapper

import com.example.core.data.model.MenuDto
import com.example.core.data.model.MenuShortDto
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant

fun MenuDto.toDomain(): Menu {
    return Menu(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl?:"",
        description = this.description?:"",
        price = this.price?:0,
        soldCount = this.soldCount?:0,
        rank = this.rank?:0,
        customizable = this.customizable,
        isAvailable = this.isAvailable,
        maxQuantity = this.maxQuantity?:0,
        stockStatus = this.stockStatus,
        stockLabel = this.stockLabel?:"",
        restaurant = this.restaurant?.toDomain()?:dummyRestaurant
    )
}

fun MenuShortDto.toDomain(restaurant: Restaurant) : Menu{
    return Menu(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl?:"",
        description = "",
        price = this.price?:0,
        soldCount = 0,
        rank = 0,
        customizable = this.customizable,
        isAvailable = this.isAvailable,
        maxQuantity = this.maxQuantity?:0,
        stockStatus = this.stockStatus,
        stockLabel = this.stockLabel?:"",
        restaurant = restaurant
    )
}