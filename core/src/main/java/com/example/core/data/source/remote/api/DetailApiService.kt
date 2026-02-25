package com.example.core.data.source.remote.api

import com.example.core.data.model.DetailMenuDto
import com.example.core.data.model.DetailRestaurantDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailApiService {

    @GET("detail/{id}")
    suspend fun getDetailRestaurant(
        @Path("id") id: String
    ): BaseResponse<DetailRestaurantDto>

    @GET("detail/menu/{id}")
    suspend fun getDetailMenu(
        @Path("id") id: String
        ): BaseResponse<DetailMenuDto>
}