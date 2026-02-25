package com.example.core.data.source.local.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity

data class MenuWithRestaurant(
    @Embedded val menu: MenuEntity,
    @Relation(
        parentColumn = "restaurantId",
        entityColumn = "id"
    )
    val restaurant: RestaurantEntity
)
