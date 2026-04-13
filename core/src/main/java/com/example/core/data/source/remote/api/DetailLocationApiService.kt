package com.example.core.data.source.remote.api

import com.example.core.data.model.DetailMenuDto
import com.example.core.data.model.DetailRestaurantDto
import com.example.core.data.model.RouteDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailLocationApiService {

    @GET("restaurants/{restId}")
    suspend fun getDetailRestaurant(
        @Path("restId") restId: String
    ): BaseResponse<DetailRestaurantDto>

    @GET("restaurants/{restId}/routes")
    suspend fun getDetailRoutes(
        @Path("restId") restId: String
    ): BaseResponse<RouteDto>

    @GET("menus/{menuId}")
    suspend fun getDetailMenu(
        @Path("menuId") menuId: String
    ): BaseResponse<DetailMenuDto>
}