package com.example.tassty.screen.recommended

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.RestaurantUiModel
import kotlinx.collections.immutable.ImmutableList

data class RecommendedUiState(
    val selectedCategoryId: String? = null,
    val allCategories: Resource<ImmutableList<CategoryUiModel>> = Resource(),
    val recommendedRestaurant: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
    val recommendedRestaurantCategories: Resource<ImmutableList<RestaurantUiModel>> = Resource(),
)

data class RecommendedInternalState(
    val selectedCategoryId: String = "CAT-001"
)
