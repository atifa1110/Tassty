package com.example.core.domain.repository

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant

interface MenuRepository {
    suspend fun getRecommendedMenus(): ResultWrapper<List<Menu>>
    suspend fun getSuggestedMenus(): ResultWrapper<List<Menu>>
}