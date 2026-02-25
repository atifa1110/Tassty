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
    private val searchNetworkDataSource: SearchNetworkDataSource,
    private val cache: RestaurantCache
) : RestaurantRepository {

    companion object {
        private const val META_KEY_RECOMMENDED = "recommended_restaurants"
        private const val META_KEY_NEARBY = "nearby_restaurants"
    }

    override suspend fun getRecommendedRestaurants(): Flow<TasstyResponse<List<Restaurant>>> = flow {
            emit(TasstyResponse.Loading)

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

    override suspend fun getRecommendedCategoryRestaurants(categoryId:String): Flow<TasstyResponse<List<Restaurant>>> = flow {
        emit(TasstyResponse.Loading)
        when (val result = remoteDataSource.getRecommendedCategoryRestaurants(categoryId)) {
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
    }.flowOn(Dispatchers.IO)

    override suspend fun getNearbyRestaurants(): Flow<TasstyResponse<List<Restaurant>>> = flow {
        emit(TasstyResponse.Loading)

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

    override suspend fun getSearchRestaurants(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>> = flow {
        emit(TasstyResponse.Loading)

        when (val result = searchNetworkDataSource.searchRestaurants(filter)) {
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

    override suspend fun getSearchRestaurantsByCategory(id: String,filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>> = flow {
        emit(TasstyResponse.Loading)

        when (val result = searchNetworkDataSource.searchRestaurantsByCategory(id,filter)) {
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


