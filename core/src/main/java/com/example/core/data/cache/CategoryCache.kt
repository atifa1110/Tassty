package com.example.core.data.cache

import com.example.core.data.model.CategoryDto
import com.example.core.data.model.VoucherDto
import kotlinx.coroutines.sync.withLock
import okhttp3.internal.cacheGet
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.forEach

@Singleton
class CategoryCache @Inject constructor() : BaseCache<CategoryDto>() {

    private val globalMap = mutableMapOf<String, CategoryDto>()

    /**
     * Simpan semua restaurant sekaligus ke cache
     */
    suspend fun saveAll(key: String, categories: List<CategoryDto>) {
        super.saveAll(key, categories) { it.id }

        mutex.withLock {
            categories.forEach { category ->
                globalMap[category.id] = category
            }
        }
    }
}
