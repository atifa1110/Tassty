package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.model.CategoryDto
import com.example.core.data.source.remote.datasource.CategoryNetworkDataSource
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Category
import com.example.core.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryNetworkDataSource
) : CategoryRepository{

    override suspend fun getAllCategories(): ResultWrapper<List<Category>> {
        return withContext(Dispatchers.IO) {

            // Ambil dari remote
            val result = remoteDataSource.getAllCategories()
            return@withContext when (result) {
                is ResultWrapper.Success -> {

                    ResultWrapper.Success(
                        result.data.map { it.toDomain() },
                        result.meta
                    )
                }
                is ResultWrapper.Error -> result
                is ResultWrapper.Loading -> result
            }
        }
    }
}
