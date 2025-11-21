package com.example.core.data.repository

import com.example.core.data.source.local.cache.RestaurantCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.RestaurantNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantMenu
import com.example.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        private const val META_KEY_SEARCH = "search_restaurants"
    }

    override suspend fun getRecommendedRestaurants(): Flow<TasstyResponse<List<Restaurant>>> = flow {
            emit(TasstyResponse.Loading)
            // Erase when using real api
            delay(1000)

            val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_RECOMMENDED)
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
            when (val result = remoteDataSource.getRecommendedRestaurants()) {
                is TasstyResponse.Success -> {
                    // save to cache
                    cache.saveAll(META_KEY_RECOMMENDED, result.data ?: emptyList())
                    cache.saveMeta(META_KEY_RECOMMENDED, result.meta)

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
        }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher


    override suspend fun getNearbyRestaurants(): Flow<TasstyResponse<List<Restaurant>>> = flow {
        emit(TasstyResponse.Loading)

        // Erase when using real api
        delay(1000)

        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_NEARBY)
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
        when (val result = remoteDataSource.getNearbyRestaurants()) {
            is TasstyResponse.Success -> {
                // save to cache
                cache.saveAll(META_KEY_NEARBY, result.data ?: emptyList())
                cache.saveMeta(META_KEY_NEARBY, result.meta)

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
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher

    override suspend fun getSearchRestaurants(): Flow<TasstyResponse<List<Restaurant>>> = flow {
        emit(TasstyResponse.Loading)

        // Erase when using real api
        delay(1000)

        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_SEARCH)
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
        when (val result = remoteDataSource.getNearbyRestaurants()) {
            is TasstyResponse.Success -> {
                // save to cache
                cache.saveAll(META_KEY_SEARCH, result.data ?: emptyList())
                cache.saveMeta(META_KEY_SEARCH, result.meta)

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
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher

    override suspend fun getSortingRestaurants(): Flow<TasstyResponse<List<RestaurantMenu>>> = flow {
        emit(TasstyResponse.Loading)

        // Erase when using real api
        delay(2000)
        when (val result = remoteDataSource.getSortingRestaurants()) {
            is TasstyResponse.Success -> {
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
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher
}


