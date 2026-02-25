package com.example.core.data.source.local.database.model

import androidx.room.Embedded
import com.example.core.data.source.local.database.entity.RestaurantEntity

data class RestaurantWithFavorite(
    @Embedded val restaurant: RestaurantEntity,
    val isFavorite: Boolean
)
