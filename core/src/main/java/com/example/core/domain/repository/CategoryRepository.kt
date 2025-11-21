package com.example.core.domain.repository

import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<TasstyResponse<List<Category>>>
}