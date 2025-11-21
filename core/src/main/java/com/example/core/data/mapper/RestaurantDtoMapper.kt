package com.example.core.data.mapper

import com.example.core.data.model.OperationalDayDto
import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.domain.model.LocationDetails
import com.example.core.domain.model.OperationalDay
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantMenu

fun RestaurantDto.toDomain(): Restaurant {
    return Restaurant(
        id = id,
        name = name,
        imageUrl = imageUrl?:"",
        category = category,
        rating = rating,
        reviewCount = reviewCount,
        deliveryTime = deliveryTime,
        rank = rank ?: 0,
        locationDetails = LocationDetails(
            fullAddress = fullAddress,
            latitude = latitude,
            longitude = longitude,
            city = city
        ),
        operationalHours = operationalHours.map { it.toDomain() }
    )
}

fun OperationalDayDto.toDomain() = OperationalDay(
    day = day,
    hours = hours
)

fun RestaurantMenuDto.toDomain(): RestaurantMenu{
    return RestaurantMenu(
        restaurant = Restaurant(
            id = id,
            name = name,
            imageUrl = imageUrl?:"",
            category = category,
            rating = rating,
            reviewCount = reviewCount,
            deliveryTime = deliveryTime,
            rank = rank ?: 0,
            locationDetails = LocationDetails(
                fullAddress = fullAddress,
                latitude = latitude,
                longitude = longitude,
                city = city
            ),
            operationalHours = operationalHours.map { it.toDomain() }
        ),
        menuList = menuList.map { it.toDomain() }
    )
}
