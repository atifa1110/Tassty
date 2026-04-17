package com.example.tassty.screen.nearby

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantUiModel
import kotlinx.collections.immutable.ImmutableList


data class NearbyUiState(
    val resource: Resource<ImmutableList<RestaurantUiModel>> = Resource()
)
