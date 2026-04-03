package com.example.core.data.source.local.mapper

import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.domain.model.Menu

fun Menu.toDatabase(restaurantId: String): MenuEntity{
    return MenuEntity(
        id = this.id,
        restaurantId = restaurantId,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        price = this.price,
        customizable = this.customizable
    )
}
