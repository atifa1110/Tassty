package com.example.core.domain.model

data class Collection(
    val id: String,
    val title: String,
    val imageUrl: String,
    val menuCount: Int,
    val isSelected: Boolean
)

data class CollectionRestaurant(
    val id: String,
    val name: String,
    val rating: Double,
    val city: String
)

data class CollectionMenu(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: Int
)

data class CollectionRestaurantWithMenu(
    val restaurant: CollectionRestaurant,
    val menus: List<CollectionMenu>
)
