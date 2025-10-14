package com.example.core.data.source.remote.datasource

import android.content.Context
import com.example.core.data.model.RestaurantDto
import com.example.core.data.source.remote.api.RestaurantApi
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.safeApiCall
import com.google.gson.Gson
import javax.inject.Inject

class RestaurantNetworkDataSource @Inject constructor(
    private val restaurantApi: RestaurantApi
) {

    suspend fun getRecommendedRestaurants(): ResultWrapper<List<RestaurantDto>> {
        return safeApiCall {
            restaurantApi.getRecommendedRestaurants()
        }
    }

    suspend fun getNearbyRestaurants(): ResultWrapper<List<RestaurantDto>> {
        return safeApiCall {
            restaurantApi.getNearbyRestaurants()
        }
    }
}