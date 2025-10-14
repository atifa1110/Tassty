package com.example.tassty.screen.home

import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.screen.search.Resource

data class HomeUiState(
    val recommendedRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val nearbyRestaurants: Resource<List<RestaurantUiModel>> = Resource(),
    val isRefreshing: Boolean = false
)