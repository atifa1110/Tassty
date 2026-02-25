package com.example.core.data.source.remote.api

import com.example.core.data.model.SetupDto
import com.example.core.data.model.UserAddressDto
import com.example.core.data.model.UserDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.SaveCardRequest
import com.example.core.data.source.remote.request.SetUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApiService {

    @GET("users/profile")
    suspend fun getUserProfile(
    ): BaseResponse<UserDto>

    @GET("users/address")
    suspend fun getUserAddress(
    ): BaseResponse<List<UserAddressDto>>


    @POST("users/stripe/setup-intent")
    suspend fun createSetupIntent(
    ): BaseResponse<SetupDto>

    @POST("users/stripe/save-card")
    suspend fun saveCard(
        @Body request: SaveCardRequest
    ): BaseResponse<Unit>

}