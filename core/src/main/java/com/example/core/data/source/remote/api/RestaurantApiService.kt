package com.example.core.data.source.remote.api

import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApiService {
    @GET("restaurants/recommendations")
    suspend fun getRecommendedRestaurants(
    ): BaseResponse<List<RestaurantDto>>

    @GET("restaurants/recommendations/{categoryId}")
    suspend fun getRecommendedCategoryRestaurants(
        @Path("categoryId") categoryId: String
    ): BaseResponse<List<RestaurantDto>>

    @GET("restaurants/nearby")
    suspend fun getNearbyRestaurants(
    ): BaseResponse<List<RestaurantDto>>
}
