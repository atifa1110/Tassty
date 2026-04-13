package com.example.tassty.screen.nearby

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantUiModel

data class NearbyUiState(
    val resource: Resource<List<RestaurantUiModel>> = Resource()
)
