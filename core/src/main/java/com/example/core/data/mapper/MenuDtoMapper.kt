package com.example.core.data.mapper

import com.example.core.data.model.MenuDto
import com.example.core.data.model.MenuShortDto
import com.example.core.data.model.OperationalDayDto
import com.example.core.data.model.RestaurantDto
import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuFullDetail

fun MenuDto.toDomain(operationalHours : List<OperationalDayDto>): Menu {
    return Menu(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        originalPrice = originalPrice,
        discountPrice = discountPrice,
        isAvailable = isAvailable,
        rating = rating,
        soldCount = soldCount,
        isBestSeller = isBestSeller,
        isRecommended = isRecommended,
        rank = rank,
        distanceMeters = distanceMeters,
        maxOrderQuantity = maxOrderQuantity,
        operationalHours = operationalHours.map { it.toDomain() }
    )
}

fun MenuShortDto.toDomain() : Menu{
    return Menu(
        id = id,
        name = name,
        description = "",
        imageUrl = imageUrl,
        originalPrice = originalPrice,
        discountPrice = discountPrice,
        isAvailable = false,
        rating = 0.0,
        soldCount = 0,
        isBestSeller = false,
        isRecommended = false,
        rank = 0,
        distanceMeters = 0,
        maxOrderQuantity = 0,
        operationalHours = emptyList()
    )
}
fun mapToMenuFullDetail(menuDto: MenuDto, restaurantDto: RestaurantDto): MenuFullDetail {
    return MenuFullDetail(
        serverId = menuDto.id,
        name = menuDto.name,
        price = menuDto.discountPrice?:menuDto.originalPrice,
        description = menuDto.description,
        imageUrl = menuDto.imageUrl,
        restaurantId = restaurantDto.id,
        restaurantName = restaurantDto.name,
        restaurantLocation = restaurantDto.city
    )
}