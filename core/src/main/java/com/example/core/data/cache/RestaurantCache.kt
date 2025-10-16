package com.example.core.data.cache

import android.util.Log
import com.example.core.data.model.MenuDto
import com.example.core.data.model.RestaurantDto
import com.example.core.domain.model.Restaurant
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
            restaurant.forEach { menu ->
                globalMap[menu.id] = menu
            }
        }
        Log.d("Diana", globalMap.toString())
    }

    suspend fun getById(id: String): RestaurantDto? = mutex.withLock {
        globalMap[id]
    }
}
