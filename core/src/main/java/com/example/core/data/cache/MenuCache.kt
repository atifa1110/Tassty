package com.example.core.data.cache

import android.R
import com.example.core.data.model.MenuDto
import com.example.core.data.model.RestaurantDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuCache @Inject constructor() : BaseCache<MenuDto>() {

    private val globalMap = mutableMapOf<String, MenuDto>()

    /**
     * Simpan semua restaurant sekaligus ke cache
     */
    suspend fun saveAll(key: String, menus: List<MenuDto>) {
        // pakai BaseCache.saveAll
        super.saveAll(key, menus) { it.id }

        // update globalMap juga di dalam mutex BaseCache
        mutex.withLock {
            menus.forEach { menu ->
                globalMap[menu.id] = menu
            }
        }
    }

    /**
     * Ambil restaurant by ID
     */
    suspend fun getById(id: String): MenuDto? = mutex.withLock {
        globalMap[id]
    }

    /**
     * Clear key tertentu
     */
//    override suspend fun clear(key: String) = mutex.withLock {
//        val ids = dataMap[key]?.keys ?: emptySet()
//        ids.forEach { globalMap.remove(it) }
//        super.clear(key)
//    }
//
//    /**
//     * Clear semua cache
//     */
//    override suspend fun clearAll() = mutex.withLock {
//        globalMap.clear()
//        super.clearAll()
//    }

}
