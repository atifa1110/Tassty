package com.example.tassty.screen.recommended

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.RestaurantUiModel

data class RecommendedUiState(
    val selectedCategoryId: String? = null,
    val allCategories: Resource<List<CategoryUiModel>> = Resource(),
    val recommendedRestaurant: Resource<List<RestaurantUiModel>> = Resource(),
    val recommendedRestaurantCategories: Resource<List<RestaurantUiModel>> = Resource(),
)

data class RecommendedInternalState(
    val selectedCategoryId: String = "CAT-001"
)
