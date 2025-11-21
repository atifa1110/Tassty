package com.example.core.data.source.remote.api

import com.example.core.data.model.MenuDto
import com.example.core.data.source.remote.network.ApiResponse
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET

interface MenuApiService {
    @GET("recommended_menu")
    suspend fun getRecommendedMenus()
    : BaseResponse<List<MenuDto>>

    @GET("suggested_menu")
    suspend fun getSuggestedMenus()
    : BaseResponse<List<MenuDto>>

    @GET("search_menu")
    suspend fun getSearchMenus()
    : BaseResponse<List<MenuDto>>
}