package com.example.core.domain.repository

import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Category

interface CategoryRepository {
    suspend fun getAllCategories(): ResultWrapper<List<Category>>
}