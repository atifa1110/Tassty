package com.example.core.data.source.local.cache

import com.example.core.data.model.CategoryDto
import com.example.core.data.model.MenuDto
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuCache @Inject constructor() : BaseCache<MenuDto>() {

    suspend fun saveMenus(key: String, menus: List<MenuDto>) {
        super.saveAll(key, menus) { it.id }
    }
}
