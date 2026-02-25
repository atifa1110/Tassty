package com.example.core.data.mapper

import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.model.RestaurantShortDto
import com.example.core.domain.model.LocationDetail
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantWithMenu

fun RestaurantDto.toDomain(): Restaurant {
    return Restaurant(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl?:"",
        fullAddress = this.fullAddress,
        latitude = this.latitude,
        longitude = this.longitude,
        city = this.city,
        rank = this.rank ?: 0,
        rating = this.rating,
        totalReviews = this.totalReviews,
        deliveryCost = 0,
        deliveryTime = this.deliveryTime,
        distance = this.distance,
        categories = this.categories,
        isOpenFromApi = this.isOpen,
        closingTimeServerFromApi = this.closingTimeServer?:""
    )
}

fun RestaurantShortDto.toDomain(): Restaurant {
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
        distance = this.distance,
        deliveryCost = this.deliveryCost?:0,
        deliveryTime = this.deliveryTime?:"",
        isOpenFromApi = this.isOpen,
        closingTimeServerFromApi = this.closingTimeServer?:""
    )
}

fun RestaurantMenuDto.toDomain(): RestaurantWithMenu {
    val restaurant = Restaurant(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl?:"",
        categories = this.categories,
        city= this.city,
        fullAddress = "",
        latitude = 0.0,
        longitude = 0.0,
        rank = this.rank?:0,
        rating = this.rating,
        totalReviews = 0,
        distance = this.distance,
        deliveryCost = this.deliveryCost?:0,
        deliveryTime = this.deliveryTime?:"",
        isOpenFromApi = this.isOpen,
        closingTimeServerFromApi = this.closingTimeServer?:""
    )

    val menus = menus.map {
        it.toDomain(restaurant)
    }

    return RestaurantWithMenu(
        restaurant = restaurant,
        menus = menus
    )
}

val dummyRestaurant = Restaurant(
    id = "",
    name = "Unknown",
    imageUrl = "",
    categories = emptyList(),
    city = "",
    fullAddress = "",
    longitude = 0.0,
    latitude = 0.0,
    rank = 0,
    rating = 0.0,
    totalReviews = 0,
    distance = 0,
    deliveryCost = 0,
    deliveryTime = "",
    isOpenFromApi = false,
    closingTimeServerFromApi = ""
)
