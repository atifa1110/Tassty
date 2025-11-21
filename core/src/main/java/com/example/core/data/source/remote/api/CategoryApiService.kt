package com.example.core.data.source.remote.api

import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET

interface CategoryApiService {
    @GET("category")
    suspend fun getAllCategories(): BaseResponse<List<CategoryDto>>
}