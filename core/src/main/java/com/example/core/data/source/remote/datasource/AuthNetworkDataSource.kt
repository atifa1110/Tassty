package com.example.core.data.source.remote.datasource

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.api.AuthAuthenticatedApiService
import com.example.core.data.source.remote.api.AuthPublicApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.EmailRequest
import com.example.core.data.source.remote.request.LoginRequest
import com.example.core.data.source.remote.request.PasswordRequest
import com.example.core.data.source.remote.request.RegisterRequest
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.data.source.remote.request.TokenRequest
import com.example.core.data.source.remote.request.VerifyCodeRequest
import javax.inject.Inject

class AuthNetworkDataSource @Inject constructor(
    private val publicApiService: AuthPublicApiService,
    private val authenticatedApiService: AuthAuthenticatedApiService
) {

    suspend fun register(email: String, password: String,fullName : String, role: String) : TasstyResponse<AuthDto>{
        return safeApiCall { publicApiService.register(RegisterRequest(fullName,email,password,role)) }
    }

    suspend fun verifyEmailOtp(email: String,code: String) : TasstyResponse<AuthDto> {
        return safeApiCall {
            publicApiService.verifyEmailOtp(VerifyCodeRequest(email, code))
        }
    }

    suspend fun resendEmailOtp(email: String) : TasstyResponse<AuthDto> {
        return safeApiCall {
            publicApiService.resendEmailOtp(EmailRequest(email))
        }
    }

    suspend fun setupAccount(request: SetUpRequest) : TasstyResponse<AuthDto>{
        return safeApiCall { authenticatedApiService.setupAccount(request) }
    }

    suspend fun login(email: String, password: String): TasstyResponse<AuthDto> {
        return safeApiCall{
            publicApiService.login(LoginRequest(email, password))
        }
    }

    suspend fun refreshToken(refreshToken: String): TasstyResponse<AuthDto> {
        return safeApiCall{
            publicApiService.refreshToken(TokenRequest(refreshToken))
        }
    }

    suspend fun forgotPassword(email: String): TasstyResponse<AuthDto> {
        return safeApiCall{
            publicApiService.forgotPassword(EmailRequest(email))
        }
    }

    suspend fun verifyResetOtp(email: String,code: String): TasstyResponse<AuthDto> {
        return safeApiCall{
            publicApiService.verifyResetOtp(VerifyCodeRequest(email,code))
        }
    }

    suspend fun resetPassword(newPassword: String): TasstyResponse<Unit> {
        return safeApiCall{
            authenticatedApiService.resetPassword(PasswordRequest(newPassword))
        }
    }

    suspend fun logout(): TasstyResponse<Unit> {
        return safeApiCall{
            authenticatedApiService.logout()
        }
    }
}