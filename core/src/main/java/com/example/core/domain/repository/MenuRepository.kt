package com.example.core.domain.repository

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    suspend fun getRecommendedMenus(): Flow<TasstyResponse<List<Menu>>>
    suspend fun getSuggestedMenus(): Flow<TasstyResponse<List<Menu>>>

    suspend fun getSearchMenus(): Flow<TasstyResponse<List<Menu>>>
}