package com.example.core.domain.repository

import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantMenu
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getRecommendedRestaurants(): Flow<TasstyResponse<List<Restaurant>>>
    suspend fun getNearbyRestaurants(): Flow<TasstyResponse<List<Restaurant>>>
    suspend fun getSearchRestaurants(): Flow<TasstyResponse<List<Restaurant>>>

    suspend fun getSortingRestaurants(): Flow<TasstyResponse<List<RestaurantMenu>>>
}