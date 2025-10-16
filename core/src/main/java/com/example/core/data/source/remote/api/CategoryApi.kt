package com.example.core.data.source.remote.api

import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.network.ApiResponse

interface CategoryApi {
    suspend fun getAllCategories(): ApiResponse<List<CategoryDto>>
}