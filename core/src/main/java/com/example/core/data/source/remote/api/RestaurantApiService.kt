package com.example.core.data.source.remote.api

import com.example.core.data.model.RestaurantDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApiService {
    @GET("recommended_restaurant")
    suspend fun getRecommendedRestaurants(
    ): BaseResponse<List<RestaurantDto>>

    @GET("nearby_restaurant")
    suspend fun getNearbyRestaurants(
    ): BaseResponse<List<RestaurantDto>>

    @GET("search_restaurant")
    suspend fun getSearchRestaurants(
    ): BaseResponse<List<RestaurantDto>>

    @GET("sorting_restaurant")
    suspend fun getSortingRestaurants(
    ): BaseResponse<List<RestaurantMenuDto>>


    suspend fun getRecommendedCategory(
        @Query("categoryName") categoryName: String
    ): BaseResponse<List<RestaurantDto>>

    suspend fun getRecommendedPersonal(
    ): BaseResponse<List<RestaurantDto>>
}
