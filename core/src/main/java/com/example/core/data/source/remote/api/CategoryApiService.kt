package com.example.core.data.source.remote.api

import com.example.core.data.model.CategoryDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApiService {
    @GET("restaurants/categories")
    suspend fun getAllCategories(): BaseResponse<List<CategoryDto>>
}