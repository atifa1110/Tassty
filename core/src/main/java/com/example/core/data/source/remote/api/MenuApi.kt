package com.example.core.data.source.remote.api

import com.example.core.data.model.MenuDto
import com.example.core.data.source.remote.network.ApiResponse

interface MenuApi {
    suspend fun getRecommendedMenus(): ApiResponse<List<MenuDto>>
    suspend fun getSuggestedMenus(): ApiResponse<List<MenuDto>>
}