package com.example.core.ui.mapper

import com.example.core.domain.model.Collection
import com.example.core.domain.model.CollectionMenu
import com.example.core.domain.model.CollectionRestaurant
import com.example.core.domain.model.CollectionRestaurantWithMenu
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.model.CollectionMenuUiModel
import com.example.core.ui.model.CollectionRestaurantUiModel
import com.example.core.ui.model.CollectionRestaurantWithMenuUiModel
import com.example.core.ui.model.CollectionUiModel

fun Collection.toUiModel() : CollectionUiModel {
    return CollectionUiModel(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        menuCount = this.menuCount,
        isSelected = false
    )
}

fun CollectionRestaurant.toUiModel(): CollectionRestaurantUiModel{
    return CollectionRestaurantUiModel(
        id= this.id,
        name = this.name,
        ratingText = this.rating.toString(),
        city = this.city
    )
}

fun CollectionMenu.toUiModel(): CollectionMenuUiModel{
    return CollectionMenuUiModel(
        id= this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        priceText = this.price.toCleanRupiahFormat()
    )
}

fun CollectionRestaurantWithMenu.toUiModel() : CollectionRestaurantWithMenuUiModel{
    return CollectionRestaurantWithMenuUiModel(
        restaurant = this.restaurant.toUiModel(),
        menus = this.menus.map { it.toUiModel() }
    )
}