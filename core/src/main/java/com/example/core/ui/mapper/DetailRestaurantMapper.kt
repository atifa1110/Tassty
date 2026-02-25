package com.example.core.ui.mapper

import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.OperationalDay
import com.example.core.domain.model.Restaurant
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.OperationalDayUi

fun DetailRestaurant.toUiModel(): DetailRestaurantUiModel{
    return DetailRestaurantUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        city = this.city,
        fullAddress = this.fullAddress,
        categories = this.categories.joinToString(", "),
        rank = this.rank,
        rating = this.rating,
        totalReviews = this.totalReviews,
        deliveryTime = this.deliveryTime,
        distance = this.distance,
        isVerified = this.isVerified,
        operationalDay = this.operationalHours.map { it.toUiModel(this.currentDay)},
        statusResult = this.statusResult,
        deliveryCost = this.deliveryCost,
        isWishlist = this.isWishlist
    )
}

fun OperationalDay.toUiModel(currentDay: String) = OperationalDayUi(
    day = day,
    hours = hours,
    isToday = day.equals(currentDay, ignoreCase = true)
)

fun OperationalDayUi.toDomain() = OperationalDay(
    day = day,
    hours = hours
)

fun DetailRestaurantUiModel.toDomain(): Restaurant{
    return Restaurant(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        fullAddress = this.fullAddress,
        city = this.city,
        longitude = 0.0,
        latitude = 0.0,
        categories = this.categories.split(", "),
        rank = this.rank,
        rating = this.rating,
        totalReviews = this.totalReviews,
        distance = this.distance,
        deliveryCost = this.deliveryCost,
        deliveryTime = this.deliveryTime,
        isOpenFromApi = false,
        closingTimeServerFromApi = "",
    )
}

fun DetailRestaurantUiModel.toDomainDetail(): DetailRestaurant{
    return DetailRestaurant(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        categories = this.categories.split(", "),
        isVerified = this.isVerified,
        city = this.city,
        fullAddress = this.fullAddress,
        longitude = 0.0,
        latitude = 0.0,
        rank = this.rank,
        rating = this.rating,
        totalReviews = this.totalReviews,
        distance = this.distance,
        deliveryCost = this.deliveryCost,
        deliveryTime = this.deliveryTime,
        operationalHours = this.operationalDay.map { it.toDomain() },
        isOpen = false,
        closingTimeServer = "",
        currentDay = "",
    )
}