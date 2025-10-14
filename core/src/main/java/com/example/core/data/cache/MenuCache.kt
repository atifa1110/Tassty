package com.example.core.data.cache

import com.example.core.data.model.MenuDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuCache @Inject constructor() : BaseCache<MenuDto>() {

    /** Ambil restaurant berdasarkan ID dari kategori tertentu */
    fun getById(key: String, id: String): MenuDto? {
        return dataMap[key]?.get(id)
    }

    /** Simpan 1 restaurant ke kategori tertentu */
    fun save(key: String, menuDto : MenuDto) {
        val categoryMap = dataMap.getOrPut(key) { mutableMapOf() }
        categoryMap[menuDto.id] = menuDto
    }

    /** Hapus satu restaurant berdasarkan ID */
    fun remove(key: String, id: String) {
        dataMap[key]?.remove(id)
    }
}
