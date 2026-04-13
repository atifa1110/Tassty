package com.example.core.data.source.remote.api

import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryLocationApiService {

    @GET("restaurants/categories/{categoryId}")
    suspend fun searchRestaurantsByCategory(
        @Path("categoryId") categoryId: String,
        @Query("query") keyword: String?,
        @Query("min_rating") minRating: String?,
        @Query("price_range") priceRange: String?,
        @Query("mode") mode: String?,
        @Query("cuisine") cuisineId: String?,
        @Query("sorting") sorting: String?
    ): BaseResponse<List<RestaurantMenuDto>>
}