package com.example.core.data.source.remote.api

import com.example.core.data.model.DetailMenuDto
import com.example.core.data.model.DetailRestaurantDto
import com.example.core.data.model.MenuDto
import com.example.core.data.model.RouteDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailApiService {

    @GET("restaurants/{restId}/menus/bestseller")
    suspend fun getDetailBestSellerMenu(
        @Path("restId") restId: String
    ): BaseResponse<List<MenuDto>>

    @GET("restaurants/{restId}/menus/recommended")
    suspend fun getDetailRecommendedMenu(
        @Path("restId") restId: String
    ): BaseResponse<List<MenuDto>>

    @GET("restaurants/{restId}/menus")
    suspend fun getDetailAllMenu(
        @Path("restId") restId: String
    ): BaseResponse<List<MenuDto>>

}