package com.example.core.data.cache

import com.example.core.data.source.remote.network.Meta
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BaseCache<T : Any> @Inject constructor() {

    // Simpan data per kategori (key: misalnya "recommended_restaurants")
    internal val dataMap = mutableMapOf<String, MutableMap<String, T>>()

    // Simpan meta per kategori
    private val metaMap = mutableMapOf<String, Meta?>()

    /** Simpan banyak data ke kategori tertentu */
    open fun saveAll(key: String, items: List<T>, getId: (T) -> String) {
        val categoryMap = dataMap.getOrPut(key) { mutableMapOf() }
        items.forEach { categoryMap[getId(it)] = it }
    }

    /** Ambil semua data berdasarkan kategori */
    open fun getAll(key: String): List<T> {
        return dataMap[key]?.values?.toList() ?: emptyList()
    }

    /** Ambil data + meta sekaligus */
    open fun getWithMeta(key: String): Pair<List<T>, Meta?> {
        return getAll(key) to getMeta(key)
    }

    /** Simpan meta */
    open fun saveMeta(key: String, meta: Meta?) {
        if (meta != null) metaMap[key] = meta
    }

    /** Ambil meta */
    open fun getMeta(key: String): Meta? = metaMap[key]

    /** Hapus cache kategori tertentu */
    open fun clear(key: String) {
        dataMap.remove(key)
        metaMap.remove(key)
    }

    /** Hapus semua cache */
    open fun clearAll() {
        dataMap.clear()
        metaMap.clear()
    }
}
