package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getRecommendedMenus(): Flow<TasstyResponse<List<Menu>>>
    fun getSuggestedMenus(): Flow<TasstyResponse<List<Menu>>>

    suspend fun getSearchMenus(): Flow<TasstyResponse<List<Menu>>>

    fun getDetailBestSellerMenu(id:String): Flow<TasstyResponse<List<Menu>>>

    fun getDetailRecommendedMenu(id:String): Flow<TasstyResponse<List<Menu>>>
    fun getDetailAllMenu(id:String): Flow<TasstyResponse<List<Menu>>>

    suspend fun runDatabaseMaintenance()
}