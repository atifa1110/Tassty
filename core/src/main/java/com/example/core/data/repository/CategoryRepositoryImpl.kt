package com.example.core.data.repository

import com.example.core.data.cache.CategoryCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.CategoryNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Category
import com.example.core.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoryNetworkDataSource,
    val cache: CategoryCache
) : CategoryRepository{

    companion object {
        private const val META_KEY_CATEGORY = "all_categories"
    }

    override suspend fun getAllCategories(): ResultWrapper<List<Category>> {
        return withContext(Dispatchers.IO) {
            val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_CATEGORY)
            if (cachedData.isNotEmpty()) {
                return@withContext ResultWrapper.Success(
                    cachedData.map { it.toDomain() },
                    cachedMeta ?: Meta(0, "", "", null)
                )
            }

            val result = remoteDataSource.getAllCategories()
            return@withContext when (result) {
                is ResultWrapper.Success -> {
                    cache.saveAll(META_KEY_CATEGORY, result.data)
                    cache.saveMeta(META_KEY_CATEGORY, result.meta)

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
