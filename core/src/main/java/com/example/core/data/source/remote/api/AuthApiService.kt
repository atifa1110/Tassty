package com.example.core.data.source.remote.api

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.LoginRequest
import com.example.core.data.source.remote.request.RegisterRequest
import com.example.core.data.source.remote.request.VerifyRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<AuthDto>


    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse<AuthDto>


    @POST("verify")
    suspend fun verifyEmail(
        @Body request: VerifyRequest
    ): BaseResponse<Nothing>
}
