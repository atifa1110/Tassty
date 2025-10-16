package com.example.core.data.source.remote.datasource

import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.api.CategoryApi
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class CategoryNetworkDataSource @Inject constructor(
    private val service: CategoryApi
){
    suspend fun getAllCategories(): ResultWrapper<List<CategoryDto>> {
        return safeApiCall {
            service.getAllCategories()
        }
    }
}