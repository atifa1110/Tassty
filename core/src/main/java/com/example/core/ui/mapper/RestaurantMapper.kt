package com.example.core.ui.mapper

import com.example.core.domain.model.LocationDetail
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantUiModel

fun Restaurant.toUiModel(): RestaurantUiModel{
    return RestaurantUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        categories = this.categories.joinToString(", "),
        rank = this.rank,
        rating = this.rating,
        totalReviews = this.totalReviews,
        deliveryCost = this.deliveryCost,
        deliveryTime = this.deliveryTime,
        distance = this.distance,
        locationDetail = LocationDetail(
            fullAddress = this.fullAddress,
            longitude = this.longitude,
            latitude = this.latitude,
            city = this.city
        ),
        statusResult = this.statusResult,
    )
}

fun RestaurantWithMenu.toUiModel(): RestaurantMenuUiModel{
    return RestaurantMenuUiModel(
        restaurant = restaurant.toUiModel(),
        menus = menus.map { it.toUiModel() }
    )
}

fun RestaurantUiModel?.toDomain(): Restaurant {
    return Restaurant(
        id = this?.id?:"",
        name = this?.name?:"",
        imageUrl = this?.imageUrl?:"",
        fullAddress = this?.locationDetail?.fullAddress?:"",
        city = this?.locationDetail?.city?:"",
        latitude = this?.locationDetail?.latitude?:0.0,
        longitude = this?.locationDetail?.longitude?:0.0,
        categories = this?.categories?.split(",")?.map { it.trim() }?:emptyList(),
        rank = this?.rank?:0,
        rating = this?.rating?:0.0,
        totalReviews = this?.totalReviews?:0,
        deliveryCost = this?.deliveryCost?:0,
        deliveryTime = this?.deliveryTime?:"",
        distance = this?.distance?:0,
        isOpenFromApi = false,
        closingTimeServerFromApi = ""
    )
}

val empty = Restaurant(
    id = "",
    name = "",
    imageUrl = "",
    fullAddress = "",
    city = "",
    latitude = 0.0,
    longitude = 0.0,
    categories = emptyList(),
    rank = 0,
    rating = 0.0,
    totalReviews = 0,
    deliveryCost = 0,
    deliveryTime = "",
    distance = 0,
    isOpenFromApi = false,
    closingTimeServerFromApi = ""
)