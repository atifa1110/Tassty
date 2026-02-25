package com.example.core.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_restaurants")
data class FavoriteRestaurantEntity(
    @PrimaryKey val restaurantId: String,
    val name: String,
    val imageUrl: String,
    val categories : String,
    val city: String,
    val distance: Int,
    val rating: Double
)
