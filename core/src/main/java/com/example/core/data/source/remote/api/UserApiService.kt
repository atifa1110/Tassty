package com.example.core.data.source.remote.api

import com.example.core.data.model.CardUserDto
import com.example.core.data.model.ProfileDto
import com.example.core.data.model.SetupDto
import com.example.core.data.model.UserAddressDto
import com.example.core.data.model.UserDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.AddressRequest
import com.example.core.data.source.remote.request.SaveCardRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApiService {

    @GET("users/profile")
    suspend fun getUserProfile(
    ): BaseResponse<UserDto>

    @Multipart
    @POST("users/profile")
    suspend fun updateUserProfile(
        @Part("name") name: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): BaseResponse<ProfileDto>

    @GET("users/addresses")
    suspend fun getUserAddress(
    ): BaseResponse<List<UserAddressDto>>

    @POST("users/addresses")
    suspend fun createUserAddress(
        @Body request: AddressRequest
    ): BaseResponse<Unit>

    @POST("users/stripe/setup-intent")
    suspend fun createSetupIntent(
    ): BaseResponse<SetupDto>

    @POST("users/stripe-cards")
    suspend fun saveCard(
        @Body request: SaveCardRequest
    ): BaseResponse<Unit>

    @GET("users/stripe-cards")
    suspend fun getUserCard(
    ): BaseResponse<List<CardUserDto>>

}