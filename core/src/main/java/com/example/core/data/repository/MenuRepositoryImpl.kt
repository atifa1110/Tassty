package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.local.cache.MenuCache
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
    private val cache: MenuCache,
    private val cleanDataSource: CleanDataSource,
    private val remoteDataSource: MenuNetworkDataSource,
): MenuRepository {

    companion object {
        private const val META_KEY_MENU_RECOMMENDED = "recommended_menus"
        private const val META_KEY_MENU_SUGGESTED = "suggested_menus"
    }

    override fun getRecommendedMenus(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Menu>>> = flow {
        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_MENU_RECOMMENDED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto -> menuDto.toDomain() }
            emit(
                TasstyResponse.Success(
                    data = menus,
                    meta = cachedMeta ?: Meta(400, "error", "Cache Empty", null)
                )
            )
        }

        val shouldFetch = cachedData.isEmpty() || fetchFromRemote
        if (shouldFetch) {
            emit(TasstyResponse.Loading())

            when (val result =  remoteDataSource.getRecommendedMenus()) {
                is TasstyResponse.Success -> {
                    cache.saveMenus(META_KEY_MENU_RECOMMENDED, result.data ?: emptyList())
                    cache.saveMeta(META_KEY_MENU_RECOMMENDED, result.meta)

                    val menus = result.data?.map { menuDto -> menuDto.toDomain() }

                    emit(TasstyResponse.Success(menus, result.meta))
                }

                is TasstyResponse.Error -> emit(result)
                else -> {}
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getSuggestedMenus(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Menu>>> = flow{
        val (cachedData, cachedMeta) = cache.getWithMeta(META_KEY_MENU_SUGGESTED)
        if (cachedData.isNotEmpty()) {
            val menus = cachedData.map { menuDto -> menuDto.toDomain() }
            emit(
                TasstyResponse.Success(
                    data = menus,
                    meta = cachedMeta ?: Meta(400, "error", "Cache Empty", null)
                )
            )
        }

        val shouldFetch = cachedData.isEmpty() || fetchFromRemote
        if (shouldFetch) {
            emit(TasstyResponse.Loading())

            when ( val result = remoteDataSource.getSuggestedMenus()) {
                is TasstyResponse.Success -> {
                    cache.saveMenus(META_KEY_MENU_SUGGESTED, result.data ?: emptyList())
                    cache.saveMeta(META_KEY_MENU_SUGGESTED, result.meta)

                    val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                    emit(TasstyResponse.Success(menus, result.meta))
                }

                is TasstyResponse.Error -> emit(result)
                else -> {}
            }
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun runDatabaseMaintenance() {
        withContext(Dispatchers.IO){
            cleanDataSource.cleanupOrphanedData()
        }
    }
}