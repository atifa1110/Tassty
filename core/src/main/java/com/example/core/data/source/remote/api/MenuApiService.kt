package com.example.core.data.source.remote.api

import com.example.core.data.model.MenuDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MenuApiService {
    @GET("menus/recommended")
    suspend fun getRecommendedMenus()
    : BaseResponse<List<MenuDto>>

    @GET("menus/suggested")
    suspend fun getSuggestedMenus()
    : BaseResponse<List<MenuDto>>

    @GET("search_menu")
    suspend fun getSearchMenus()
    : BaseResponse<List<MenuDto>>

    @GET("detail/{id}/menus/bestseller")
    suspend fun getDetailBestSellerMenu(
        @Path("id") id: String
    ): BaseResponse<List<MenuDto>>

    @GET("detail/{id}/menus/recommended")
    suspend fun getDetailRecommendedMenu(
        @Path("id") id: String
    ): BaseResponse<List<MenuDto>>

    @GET("detail/{id}/menus/all")
    suspend fun getDetailAllMenu(
        @Path("id") id: String
    ): BaseResponse<List<MenuDto>>
}