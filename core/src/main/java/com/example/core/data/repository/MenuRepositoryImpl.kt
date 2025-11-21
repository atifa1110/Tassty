package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.local.datasource.MenuCacheLocalDataSource
import com.example.core.data.source.remote.datasource.MenuNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val remoteDataSource: MenuNetworkDataSource,
    private val menuCacheLocalDataSource: MenuCacheLocalDataSource,
): MenuRepository {

    companion object {
        private const val META_KEY_MENU_RECOMMENDED = "recommended_menus"
        private const val META_KEY_MENU_SUGGESTED = "suggested_menus"
    }


    override suspend fun getRecommendedMenus(): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading)

        // Erase when using real api
        delay(1000)

        val (cachedData, cachedMeta) = menuCacheLocalDataSource.getWithMeta(META_KEY_MENU_RECOMMENDED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto ->
                val restaurantOps = menuCacheLocalDataSource.getOperationalHoursForMenu(menuDto)
                menuDto.toDomain(restaurantOps)
            }
            emit(
                TasstyResponse.Success(
                    data = menus,
                    meta = cachedMeta ?: Meta(0, "", "", null)
                )
            )
            return@flow
        }

        val result = remoteDataSource.getRecommendedMenus()
        when (result) {
            is TasstyResponse.Success -> {
                // save to cache + meta
                menuCacheLocalDataSource.saveMenu(META_KEY_MENU_RECOMMENDED, result.data?: emptyList())
                menuCacheLocalDataSource.saveMeta(META_KEY_MENU_RECOMMENDED, result.meta)

                val menus = result.data?.map { menuDto ->
                    val restaurantOps = menuCacheLocalDataSource.getOperationalHoursForMenu(menuDto)
                    menuDto.toDomain(restaurantOps)
                }

                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher



    override suspend fun getSuggestedMenus(): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading)
        // Erase when using real api
        delay(1000)

        val (cachedData, cachedMeta) = menuCacheLocalDataSource.getWithMeta(META_KEY_MENU_SUGGESTED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto ->
                val restaurantOps = menuCacheLocalDataSource.getOperationalHoursForMenu(menuDto)
                menuDto.toDomain(restaurantOps)
            }
            emit(
                TasstyResponse.Success(
                    data = menus,
                    meta = cachedMeta ?: Meta(0, "", "", null)
                )
            )
            return@flow
        }

        val result = remoteDataSource.getSuggestedMenus()
        when (result) {
            is TasstyResponse.Success -> {
                // save to cache + meta
                menuCacheLocalDataSource.saveMenu(META_KEY_MENU_SUGGESTED, result.data?: emptyList())
                menuCacheLocalDataSource.saveMeta(META_KEY_MENU_SUGGESTED, result.meta)

                val menus = result.data?.map { menuDto ->
                    val restaurantOps = menuCacheLocalDataSource.getOperationalHoursForMenu(menuDto)
                    menuDto.toDomain(restaurantOps)
                }

                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher

    override suspend fun getSearchMenus(): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading)
        // Erase when using real api
        delay(1000)

        val result = remoteDataSource.getSearchMenus()
        when (result) {
            is TasstyResponse.Success -> {

                val menus = result.data?.map { menuDto ->
                    val restaurantOps = menuCacheLocalDataSource.getOperationalHoursForMenu(menuDto)
                    menuDto.toDomain(restaurantOps)
                }

                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher


}