package com.example.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_items")
data class RestaurantEntity(
    @PrimaryKey val restaurantId: Int,
    val name: String,
    val city: String,
    val rating: Double
)