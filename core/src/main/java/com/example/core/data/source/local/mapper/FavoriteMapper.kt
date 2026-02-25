package com.example.core.data.source.local.mapper

import com.example.core.data.source.local.database.entity.FavoriteRestaurantEntity
import com.example.core.domain.model.Favorite

fun FavoriteRestaurantEntity.toDomain(): Favorite{
    return Favorite(
        id = this.restaurantId,
        name = this.name,
        imageUrl = this.imageUrl,
        categories = this.categories,
        city = this.city,
        distance = this.distance,
        rating = this.rating
    )
}