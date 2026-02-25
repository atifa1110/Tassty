package com.example.core.ui.model

data class CollectionUiModel (
    val id: String,
    val title: String,
    val imageUrl: String,
    val menuCount: Int,
    val isSelected: Boolean
)

data class CollectionRestaurantUiModel(
    val id: String,
    val name: String,
    val ratingText: String,
    val city: String
)

data class CollectionMenuUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val priceText: String
)

data class CollectionRestaurantWithMenuUiModel(
    val restaurant: CollectionRestaurantUiModel,
    val menus: List<CollectionMenuUiModel>
)

