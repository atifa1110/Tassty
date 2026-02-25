package com.example.core.data.source.local.mapper

import com.example.core.data.source.local.database.entity.FavoriteRestaurantEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.Restaurant
import com.example.core.ui.mapper.empty

fun Restaurant.toDatabase(): RestaurantEntity{
    return RestaurantEntity(
        id = this.id,
        name = this.name,
        rating = this.rating,
        city = this.city,
        deliveryCost = this.deliveryCost
    )
}

fun DetailRestaurant.toDatabase(): FavoriteRestaurantEntity{
    return FavoriteRestaurantEntity(
        restaurantId = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        categories = this.categories.joinToString(", "),
        city = this.city,
        rating = this.rating,
        distance = this.distance
    )
}

fun RestaurantEntity.toDomainRestaurant() : Restaurant{
    return Restaurant(
        id = this.id,
        name = this.name,
        imageUrl = "",
        categories = emptyList(),
        city = this.city,
        fullAddress = "",
        latitude = 0.0,
        longitude = 0.0,
        rank = 0,
        rating = this.rating,
        totalReviews = 0,
        distance = 0,
        deliveryCost = this.deliveryCost,
        deliveryTime = "",
        isOpenFromApi=false,
        closingTimeServerFromApi=""
    )
}