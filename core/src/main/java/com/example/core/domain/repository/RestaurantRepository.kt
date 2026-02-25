package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getRecommendedRestaurants(): Flow<TasstyResponse<List<Restaurant>>>
    suspend fun getRecommendedCategoryRestaurants(categoryId:String): Flow<TasstyResponse<List<Restaurant>>>
    suspend fun getNearbyRestaurants(): Flow<TasstyResponse<List<Restaurant>>>
    suspend fun getSearchRestaurants(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>>
    suspend fun getSearchRestaurantsByCategory(id: String,filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>>
}