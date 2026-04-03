package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRecommendedRestaurants(): Flow<TasstyResponse<List<Restaurant>>>
    fun getRecommendedCategoryRestaurants(categoryId:String): Flow<TasstyResponse<List<Restaurant>>>
    fun getNearbyRestaurants(): Flow<TasstyResponse<List<Restaurant>>>
    fun getSearchRestaurants(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>>
    fun getSearchRestaurantsByCategory(id: String,filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>>
}