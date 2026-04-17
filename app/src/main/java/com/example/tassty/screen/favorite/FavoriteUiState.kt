package com.example.tassty.screen.favorite

import com.example.core.ui.model.RestaurantUiModel
import kotlinx.collections.immutable.ImmutableList

data class FavoriteUiState (
    val resource : ImmutableList<RestaurantUiModel>? = null
)