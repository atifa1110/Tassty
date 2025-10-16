package com.example.core.data.repository

import android.util.Log
import com.example.core.data.cache.RestaurantCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.RestaurantNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val remoteDataSource: RestaurantNetworkDataSource,
    private val cache: RestaurantCache
) : RestaurantRepository {

    companion object {
        private const val META_KEY_RECOMMENDED = "recommended_restaurants"
        private const val META_KEY_NEARBY = "nearby_restaurants"
    }

    override suspend fun getRecommendedRestaurants(): ResultWrapper<List<Restaurant>> {
        return withContext(Dispatchers.IO) {
            val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_RECOMMENDED)
            Log.d("Diana", "cachedData=${cachedData.size}")

            if (cachedData.isNotEmpty()) {
                // Return cache hit
                return@withContext ResultWrapper.Success(
                    cachedData.map { it.toDomain() },
                    cachedMeta ?: Meta(0, "", "", null)
                )
            }

            // Ambil dari remote
            val result = remoteDataSource.getRecommendedRestaurants()
            return@withContext when (result) {
                is ResultWrapper.Success -> {
                    Log.d("Diana", "API Success: ${result.data}")

                    cache.saveAll(META_KEY_RECOMMENDED,result.data)
                    cache.saveMeta(META_KEY_RECOMMENDED,result.meta)

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

    override suspend fun getNearbyRestaurants(): ResultWrapper<List<Restaurant>> =
        withContext(Dispatchers.IO) {
            val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_NEARBY)
            if (cachedData.isNotEmpty()) {
                return@withContext ResultWrapper.Success(
                    cachedData.map { it.toDomain() },
                    cachedMeta ?: Meta(0, "", "", null)
                )
            }

            val result = remoteDataSource.getNearbyRestaurants()
            return@withContext when (result) {
                is ResultWrapper.Success -> {
                    cache.saveAll(META_KEY_NEARBY,result.data)
                    cache.saveMeta(META_KEY_NEARBY,result.meta)
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
