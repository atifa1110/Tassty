package com.example.tassty.screen.favorite

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantUiModel

data class FavoriteUiState (
    val resource : Resource<List<RestaurantUiModel>> = Resource()
)