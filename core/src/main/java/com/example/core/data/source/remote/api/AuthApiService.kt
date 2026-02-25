package com.example.core.data.source.remote.api

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.EditRequest
import com.example.core.data.source.remote.request.LoginRequest
import com.example.core.data.source.remote.request.RegisterRequest
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.data.source.remote.request.VerifyCodeRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {

    @POST("auth/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse<AuthDto>

    @POST("auth/verify")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequest
    ): BaseResponse<AuthDto>

    @FormUrlEncoded
    @POST("auth/resend")
    suspend fun resendCode(
        @Field("email") email: String
    ): BaseResponse<Nothing>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body refreshToken: String
    ): BaseResponse<Nothing>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<AuthDto>

    @POST("auth/setup")
    suspend fun setup(
        @Body request: SetUpRequest
    ): BaseResponse<AuthDto>

    @FormUrlEncoded
    @POST("auth/registerDevice")
    suspend fun registerDevice(
        @Field("fcmToken") fcmToken: String,
        @Field("deviceType") deviceType: String
    ): BaseResponse<Nothing>?

    @PUT("edit")
    suspend fun editProfile(
        @Body request: EditRequest
    ): BaseResponse<Nothing>

    @FormUrlEncoded
    @POST("request")
    suspend fun requestResetPassword(
        @Field("email") email: String
    ): BaseResponse<Nothing>

    @FormUrlEncoded
    @POST("reset")
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("newPassword") newPassword: String
    ): BaseResponse<Nothing>

    @POST("logout")
    suspend fun logout(
    ): BaseResponse<Nothing>
}
