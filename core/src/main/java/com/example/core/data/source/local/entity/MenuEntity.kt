package com.example.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["restaurantId"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class MenuEntity(
    @PrimaryKey val menuId: Int,
    val restaurantId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)