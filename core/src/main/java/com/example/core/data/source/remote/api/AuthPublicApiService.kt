package com.example.core.data.source.remote.api

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.EmailRequest
import com.example.core.data.source.remote.request.LoginRequest
import com.example.core.data.source.remote.request.PasswordRequest
import com.example.core.data.source.remote.request.RegisterRequest
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.data.source.remote.request.TokenRequest
import com.example.core.data.source.remote.request.VerifyCodeRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthPublicApiService {

    @POST("auth/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse<AuthDto>

    @POST("auth/verify-email-otp")
    suspend fun verifyEmailOtp(
        @Body request: VerifyCodeRequest
    ): BaseResponse<AuthDto>

    @POST("auth/resend-otp")
    suspend fun resendEmailOtp(
        @Body request: EmailRequest
    ): BaseResponse<AuthDto>


    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<AuthDto>

    @POST("auth/refresh-token")
    suspend fun refreshToken(
        @Body request: TokenRequest
    ): BaseResponse<AuthDto>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: EmailRequest
    ): BaseResponse<AuthDto>

    @POST("auth/verify-reset-otp")
    suspend fun verifyResetOtp(
        @Body request: VerifyCodeRequest
    ): BaseResponse<AuthDto>
}
