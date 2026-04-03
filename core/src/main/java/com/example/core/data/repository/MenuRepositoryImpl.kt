package com.example.core.data.repository

import android.util.Log
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.local.cache.MenuCache
import com.example.core.data.source.local.database.dao.CleanupDao
import com.example.core.data.source.local.datasource.CleanDataSource
import com.example.core.data.source.remote.datasource.MenuNetworkDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val remoteDataSource: MenuNetworkDataSource,
    private val cache: MenuCache,
    private val cleanDataSource: CleanDataSource
): MenuRepository {

    companion object {
        private const val META_KEY_MENU_RECOMMENDED = "recommended_menus"
        private const val META_KEY_MENU_SUGGESTED = "suggested_menus"
    }

    override fun getRecommendedMenus(): Flow<TasstyResponse<List<Menu>>> = flow {
        emit(TasstyResponse.Loading())

        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_MENU_RECOMMENDED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto ->
                menuDto.toDomain()
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
                cache.saveAll(META_KEY_MENU_RECOMMENDED, result.data?: emptyList())
                cache.saveMeta(META_KEY_MENU_RECOMMENDED, result.meta)

                val menus = result.data?.map { menuDto ->
                    menuDto.toDomain()
                }

                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getSuggestedMenus(): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_MENU_SUGGESTED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto ->
                menuDto.toDomain()
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
                cache.saveAll(META_KEY_MENU_SUGGESTED, result.data?: emptyList())
                cache.saveMeta(META_KEY_MENU_SUGGESTED, result.meta)

                val menus = result.data?.map { menuDto ->
                    menuDto.toDomain()
                }
                Log.d("MenuRepositoryImpl","Menu: ${menus}")

                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getSearchMenus(): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = remoteDataSource.getSearchMenus()
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher

    override fun getDetailBestSellerMenu(id:String): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = remoteDataSource.getDetailBestSellerMenu(id)
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailRecommendedMenu(id: String): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = remoteDataSource.getDetailRecommendedMenu(id)
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO) // Flow running in IO dispatcher

    override fun getDetailAllMenu(id: String): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = remoteDataSource.getDetailAllMenu(id)
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun runDatabaseMaintenance() {
        withContext(Dispatchers.IO){
            cleanDataSource.cleanupOrphanedData()
        }
    }
}