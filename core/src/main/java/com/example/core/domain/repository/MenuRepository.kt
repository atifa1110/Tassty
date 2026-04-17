package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getRecommendedMenus(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Menu>>>

    fun getSuggestedMenus(fetchFromRemote: Boolean): Flow<TasstyResponse<List<Menu>>>

    suspend fun runDatabaseMaintenance()
}