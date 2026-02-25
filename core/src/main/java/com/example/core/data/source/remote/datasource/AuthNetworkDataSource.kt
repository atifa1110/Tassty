package com.example.core.data.source.remote.datasource

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.api.AuthApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.LoginRequest
import com.example.core.data.source.remote.request.RegisterRequest
import com.example.core.data.source.remote.request.SetUpRequest
import com.example.core.data.source.remote.request.VerifyCodeRequest
import javax.inject.Inject

class AuthNetworkDataSource @Inject constructor(
    private val authApi: AuthApiService
) {
    suspend fun login(email: String, password: String): TasstyResponse<AuthDto> {
        return safeApiCall{
            authApi.login(LoginRequest(email, password))
        }
    }

    suspend fun register(email: String, password: String,fullName : String, role: String) : TasstyResponse<AuthDto>{
        return safeApiCall { authApi.register(RegisterRequest(fullName,email,password,role)) }
    }

    suspend fun verification(email: String,verificationCode: String) : TasstyResponse<AuthDto>{
        return safeApiCall {
            authApi.verifyCode(VerifyCodeRequest(email,verificationCode))
        }
    }

    suspend fun resend(email: String) : TasstyResponse<Unit>{
        return safeApiCall {
            authApi.resendCode(email)
        }
    }

    suspend fun setupAccount(setUpRequest: SetUpRequest) : TasstyResponse<AuthDto>{
        return safeApiCall { authApi.setup(setUpRequest) }
    }
}
