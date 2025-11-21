package com.example.core.data.source.remote.datasource

import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.api.RestaurantApiService
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall2
import javax.inject.Inject

class RestaurantNetworkDataSource @Inject constructor(
    private val restaurantApi: RestaurantApiService
) {

    suspend fun getRecommendedRestaurants(): TasstyResponse<List<RestaurantDto>> {
        return safeApiCall2 {
            restaurantApi.getRecommendedRestaurants()
        }
    }

    suspend fun getNearbyRestaurants(): TasstyResponse<List<RestaurantDto>> {
        return safeApiCall2 {
            restaurantApi.getNearbyRestaurants()
        }
    }

    suspend fun getSearchRestaurants(): TasstyResponse<List<RestaurantDto>>{
        return safeApiCall2 {
            restaurantApi.getSearchRestaurants()
        }
    }

    suspend fun getSortingRestaurants(): TasstyResponse<List<RestaurantMenuDto>>{
        return safeApiCall2 {
            restaurantApi.getSortingRestaurants()
        }
    }

}