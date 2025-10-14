package com.example.core.data.repository

import com.example.core.data.cache.MenuCache
import com.example.core.data.cache.RestaurantCache
import com.example.core.data.mapper.toDomain
import com.example.core.data.model.MenuDto
import com.example.core.data.model.OperationalDayDto
import com.example.core.data.source.remote.datasource.MenuNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.MenuRepository
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val remoteDataSource: MenuNetworkDataSource,
    private val cache: MenuCache,
    private val restaurantCache: RestaurantCache
): MenuRepository {

    companion object {
        private const val META_KEY_RECOMMENDED = "recommended_menus"
        private const val META_KEY_SUGGESTED = "suggested_menus"
    }

    override suspend fun getRecommendedMenus(): ResultWrapper<List<Menu>> {
        // check cache
        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_RECOMMENDED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto ->
                val restaurantOps = getOperationalHoursForMenu(menuDto)
                menuDto.toDomain(restaurantOps)
            }
            return ResultWrapper.Success(menus, cachedMeta ?: Meta(0, "", "", null))
        }

        val result = remoteDataSource.getRecommendedMenus()
        return when (result) {
            is ResultWrapper.Success -> {
                // save to cache + meta
                cache.saveAll(META_KEY_RECOMMENDED, result.data) { it.id }
                cache.saveMeta(META_KEY_RECOMMENDED, result.meta)

                val menus = result.data.map { menuDto ->
                    val restaurantOps = getOperationalHoursForMenu(menuDto)
                    menuDto.toDomain(restaurantOps)
                }

                ResultWrapper.Success(menus, result.meta)
            }
            is ResultWrapper.Error -> result
            is ResultWrapper.Loading -> result
        }
    }

    override suspend fun getSuggestedMenus(): ResultWrapper<List<Menu>> {
        // check cache
        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_RECOMMENDED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto ->
                val restaurantOps = getOperationalHoursForMenu(menuDto)
                menuDto.toDomain(restaurantOps)
            }
            return ResultWrapper.Success(menus, cachedMeta ?: Meta(0, "", "", null))
        }

        val result = remoteDataSource.getSuggestedMenus()
        return when (result) {
            is ResultWrapper.Success -> {
                // save to cache + meta
                cache.saveAll(META_KEY_SUGGESTED, result.data) { it.id }
                cache.saveMeta(META_KEY_SUGGESTED, result.meta)

                val menus = result.data.map { menuDto ->
                    val restaurantOps = getOperationalHoursForMenu(menuDto)
                    menuDto.toDomain(restaurantOps)
                }

                ResultWrapper.Success(menus, result.meta)
            }
            is ResultWrapper.Error -> result
            is ResultWrapper.Loading -> result
        }
    }

    /** Ambil operationalHours restoran dari cache, fallback ke emptyList() */
    private fun getOperationalHoursForMenu(menuDto: MenuDto): List<OperationalDayDto> {
        return restaurantCache.getById(menuDto.restaurantId)?.operationalHours ?: emptyList()
    }
}