package com.example.core.data.source.remote.api

import com.example.core.data.model.RestaurantDto
import com.example.core.data.source.remote.network.ApiResponse

interface RestaurantApi {
    suspend fun getRestaurants(): ApiResponse<List<RestaurantDto>>
}
