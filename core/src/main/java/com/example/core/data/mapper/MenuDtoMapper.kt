package com.example.core.data.mapper

import com.example.core.data.model.MenuDto
import com.example.core.data.model.OperationalDayDto
import com.example.core.domain.model.Menu

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