package com.example.core.data.source.remote.datasource

import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.api.CategoryApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall2
import javax.inject.Inject

class CategoryNetworkDataSource @Inject constructor(
    private val service: CategoryApiService
){
    suspend fun getAllCategories(): TasstyResponse<List<CategoryDto>> {
        return safeApiCall2 {
            service.getAllCategories()
        }
    }
}