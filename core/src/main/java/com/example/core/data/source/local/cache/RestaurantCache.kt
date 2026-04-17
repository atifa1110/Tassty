package com.example.core.data.source.local.cache

import android.util.Log
import com.example.core.data.model.RestaurantDto
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.forEach

@Singleton
class RestaurantCache @Inject constructor() : BaseCache<RestaurantDto>() {
    private val globalMap = mutableMapOf<String, RestaurantDto>()

    suspend fun saveAll(key: String, restaurant: List<RestaurantDto>) {
        // pakai BaseCache.saveAll
        super.saveAll(key, restaurant) { it.id }
        // update globalMap juga di dalam mutex BaseCache
        mutex.withLock {
            restaurant.forEach { rest ->
                globalMap[rest.id] = rest
            }
        }
        //Log.d("DianaBaseCache", globalMap.toString())
    }
}
