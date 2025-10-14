package com.example.core.data.cache

import com.example.core.data.model.RestaurantDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantCache @Inject constructor() : BaseCache<RestaurantDto>() {

    // Cache global untuk ambil restaurant by id tanpa key
    private val globalMap = mutableMapOf<String, RestaurantDto>()

    /** Ambil restaurant berdasarkan ID dari kategori tertentu */
    fun getById(key: String, id: String): RestaurantDto? {
        return dataMap[key]?.get(id)
    }

    /** Ambil restaurant berdasarkan ID langsung (tanpa key) */
    fun getById(id: String): RestaurantDto? {
        return globalMap[id]
    }

    /** Simpan 1 restaurant ke kategori tertentu */
    fun save(key: String, restaurant: RestaurantDto) {
        // 1️⃣ simpan ke kategori
        val categoryMap = dataMap.getOrPut(key) { mutableMapOf() }
        categoryMap[restaurant.id] = restaurant

        // 2️⃣ simpan ke global
        globalMap[restaurant.id] = restaurant
    }

    /** Simpan banyak restaurant ke kategori tertentu */
    fun saveAll(key: String, restaurants: List<RestaurantDto>) {
        val categoryMap = dataMap.getOrPut(key) { mutableMapOf() }
        for (restaurant in restaurants) {
            categoryMap[restaurant.id] = restaurant
            globalMap[restaurant.id] = restaurant
        }
    }

    /** Hapus satu restaurant berdasarkan ID dari kategori tertentu */
    fun remove(key: String, id: String) {
        dataMap[key]?.remove(id)
        globalMap.remove(id)
    }

    /** Hapus semua cache kategori tertentu */
    override fun clear(key: String) {
        val ids = dataMap[key]?.keys ?: emptySet()
        for (id in ids) globalMap.remove(id)
        super.clear(key)
    }

    /** Hapus semua cache */
    override fun clearAll() {
        globalMap.clear()
        super.clearAll()
    }
}


