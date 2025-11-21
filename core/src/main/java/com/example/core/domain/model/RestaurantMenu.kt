package com.example.core.domain.model

data class RestaurantMenu (
    val restaurant : Restaurant,
    val menuList: List<Menu>
)

data class RestaurantBusinessMenu (
    val restaurant : RestaurantBusinessInfo,
    val menuList: List<MenuBusinessInfo>
)