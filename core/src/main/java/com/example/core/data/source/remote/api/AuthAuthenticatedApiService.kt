package com.example.core.data.source.remote.api

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.PasswordRequest
import com.example.core.data.source.remote.request.SetUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAuthenticatedApiService {

    @POST("auth/setup-account")
    suspend fun setupAccount(
        @Body request: SetUpRequest
    ): BaseResponse<AuthDto>

    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body request: PasswordRequest
    ): BaseResponse<Unit>

    @POST("logout")
    suspend fun logout(
    ): BaseResponse<Unit>
}