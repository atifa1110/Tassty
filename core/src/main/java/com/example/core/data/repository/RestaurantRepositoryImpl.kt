package com.example.core.data.repository

import com.example.core.data.source.local.cache.RestaurantCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.RestaurantNetworkDataSource
import com.example.core.data.source.remote.datasource.SearchNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val remoteDataSource: RestaurantNetworkDataSource,
    private val cache: RestaurantCache
) : RestaurantRepository {

    companion object {
        private const val META_KEY_RECOMMENDED = "recommended_restaurants"
        private const val META_KEY_NEARBY = "nearby_restaurants"
    }

    override fun getRecommendedRestaurants(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Restaurant>>> = flow {
            val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_RECOMMENDED)
            if (cachedData.isNotEmpty()) {
                emit(
                    TasstyResponse.Success(
                        data = cachedData.map { it.toDomain() },
                        meta = cachedMeta ?: Meta(0, "", "", null)
                    )
                )
            }

        val shouldFetch = cachedData.isEmpty() || fetchFromRemote
        if (shouldFetch) {
            emit(TasstyResponse.Loading())
            // Take from remote
            when (val result = remoteDataSource.getRecommendedRestaurants()) {
                is TasstyResponse.Success -> {
                    // save to cache
                    cache.saveRestaurants(META_KEY_RECOMMENDED, result.data ?: emptyList())
                    cache.saveMeta(META_KEY_RECOMMENDED, result.meta)

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
        }
        }.flowOn(Dispatchers.IO)

    override fun getRecommendedCategoryRestaurants(categoryId:String): Flow<TasstyResponse<List<Restaurant>>> = flow {
        emit(TasstyResponse.Loading())
        when (val result = remoteDataSource.getRecommendedCategoryRestaurants(categoryId)) {
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

    override fun getNearbyRestaurants(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Restaurant>>> = flow {
        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_NEARBY)
        if (cachedData.isNotEmpty()) {
            emit(
                TasstyResponse.Success(
                    data = cachedData.map { it.toDomain() },
                    meta = cachedMeta ?: Meta(0, "", "", null)
                )
            )
        }

        val shouldFetch = cachedData.isEmpty() || fetchFromRemote
        if (shouldFetch) {
            emit(TasstyResponse.Loading())
            when (val result = remoteDataSource.getNearbyRestaurants()) {
                is TasstyResponse.Success -> {
                    cache.saveRestaurants(META_KEY_NEARBY, result.data ?: emptyList())
                    cache.saveMeta(META_KEY_NEARBY, result.meta)

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
        }
    }.flowOn(Dispatchers.IO)
}


