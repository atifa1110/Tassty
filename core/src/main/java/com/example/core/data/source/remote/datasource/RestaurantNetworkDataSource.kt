package com.example.core.data.source.remote.datasource

import com.example.core.data.model.RestaurantDto
import com.example.core.data.source.remote.api.RestaurantApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class RestaurantNetworkDataSource @Inject constructor(
    private val restaurantApi: RestaurantApiService
) {
    suspend fun getRecommendedRestaurants(): TasstyResponse<List<RestaurantDto>> {
        return safeApiCall{ restaurantApi.getRecommendedRestaurants() }
    }

    suspend fun getRecommendedCategoryRestaurants(categoryId: String): TasstyResponse<List<RestaurantDto>> {
        return safeApiCall{ restaurantApi.getRecommendedCategoryRestaurants(categoryId) }
    }

    suspend fun getNearbyRestaurants(): TasstyResponse<List<RestaurantDto>> {
        return safeApiCall{ restaurantApi.getNearbyRestaurants() }
    }
}