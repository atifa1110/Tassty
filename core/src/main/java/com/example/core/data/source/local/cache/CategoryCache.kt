package com.example.core.data.source.local.cache

import com.example.core.data.model.CategoryDto
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.forEach

@Singleton
class CategoryCache @Inject constructor() : BaseCache<CategoryDto>() {

    private val globalMap = mutableMapOf<String, CategoryDto>()

    suspend fun saveAll(key: String, categories: List<CategoryDto>) {
        super.saveAll(key, categories) { it.id }

        mutex.withLock {
            categories.forEach { category ->
                globalMap[category.id] = category
            }
        }
    }
}
