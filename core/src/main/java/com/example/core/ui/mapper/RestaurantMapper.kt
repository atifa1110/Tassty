package com.example.core.ui.mapper

import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.ui.model.RestaurantUiModel

fun RestaurantBusinessInfo.toUiModel(): RestaurantUiModel {
    return RestaurantUiModel(
        restaurant = restaurant,
        distance = distance,
        operationalStatus = status
    )
}
