package com.example.core.data.source.local.cache

import com.example.core.data.model.MenuDto
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuCache @Inject constructor() : BaseCache<MenuDto>() {

    private val globalMap = mutableMapOf<String, MenuDto>()
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

    suspend fun getById(id: String): MenuDto? = mutex.withLock {
        globalMap[id]
    }
}
