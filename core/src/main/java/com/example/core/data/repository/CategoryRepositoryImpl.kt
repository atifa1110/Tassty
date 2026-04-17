package com.example.core.data.repository

import com.example.core.data.source.local.cache.CategoryCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.CategoryNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Category
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.CategoryRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dataSource: CategoryNetworkDataSource,
    val cache: CategoryCache
) : CategoryRepository{

    companion object {
        private const val META_KEY_CATEGORY = "all_categories"
    }

    override fun getAllCategories(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Category>>> = flow {
        emit(TasstyResponse.Loading())
        // Cek cache
        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_CATEGORY)
        if (cachedData.isNotEmpty()) {
            emit(
                TasstyResponse.Success(
                    data = cachedData.map { it.toDomain() },
                    meta = cachedMeta ?: Meta(0, "", "", null)
                )
            )
            return@flow
        }

        // Take from remote
        when (val result = dataSource.getAllCategories()) {
            is TasstyResponse.Success -> {
                // save to cache
                cache.saveAll(META_KEY_CATEGORY, result.data?:emptyList())
                cache.saveMeta(META_KEY_CATEGORY, result.meta)

                emit(
                    TasstyResponse.Success(
                        data = result.data?.map { it.toDomain() },
                        meta = result.meta
                    )
                )
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getSearchRestaurantsByCategory(
        id: String,
        filter: RestaurantSearchFilter
    ): Flow<TasstyResponse<List<RestaurantWithMenu>>> = flow {
        emit(TasstyResponse.Loading())

        when (val result = dataSource.searchRestaurantsByCategory(categoryId = id, filter = filter)) {
            is TasstyResponse.Success -> {
                emit(
                    TasstyResponse.Success(
                        data = result.data?.map { it.toDomain() },
                        meta = result.meta
                    )
                )
            }

            is TasstyResponse.Error -> emit(TasstyResponse.Error(result.meta))
            else -> {}
        }
    }.flowOn(Dispatchers.IO)
}
