package com.example.core.data.source.local.datasource

import com.example.core.data.mapper.mapToMenuFullDetail
import com.example.core.data.model.MenuDto
import com.example.core.data.model.OperationalDayDto
import com.example.core.data.source.local.cache.MenuCache
import com.example.core.data.source.local.cache.RestaurantCache
import com.example.core.data.source.remote.network.Meta
import com.example.core.domain.model.MenuFullDetail
import javax.inject.Inject

class MenuCacheLocalDataSource @Inject constructor(
    private val menuCache: MenuCache,
    private val restaurantCache: RestaurantCache
) {

    suspend fun getWithMeta(key: String) = menuCache.getWithMeta(key)
    suspend fun saveMenu(key: String, menus: List<MenuDto>) = menuCache.saveAll(key, menus)
    suspend fun saveMeta(key: String, meta: Meta) = menuCache.saveMeta(key,meta)

    suspend fun getOperationalHoursForMenu(menuDto: MenuDto): List<OperationalDayDto> {
        val restaurant = restaurantCache.getById(menuDto.restaurantId)
        return restaurant?.operationalHours ?: emptyList()
    }

    suspend fun getFullDetailFromCache(menuId: String): MenuFullDetail {
        // 1. Ambil MenuDto dari MenuCache
        val menuDto = menuCache.getById(menuId)
            ?: throw NoSuchElementException("Menu tidak ditemukan di RAM.")

        // 2. Ambil RestaurantDto dari RestaurantCache
        val restaurantDto = restaurantCache.getById(menuDto.restaurantId)
            ?: throw NoSuchElementException("Restoran tidak ditemukan di RAM.")

        // 3. Gabungkan dan kembalikan MenuFullDetail
        return mapToMenuFullDetail(menuDto, restaurantDto)
    }
}
