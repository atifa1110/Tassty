package com.example.core.domain.repository

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Restaurant

interface RestaurantRepository {
    suspend fun getRecommendedRestaurants(): ResultWrapper<List<Restaurant>>
    suspend fun getNearbyRestaurants(): ResultWrapper<List<Restaurant>>
}