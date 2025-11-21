package com.example.core.data.source.remote.datasource

import com.example.core.data.model.AuthDto
import com.example.core.data.source.remote.api.AuthApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall2
import com.example.core.data.source.remote.request.LoginRequest
import com.example.core.data.source.remote.request.RegisterRequest
import com.example.core.data.source.remote.request.VerifyRequest
import javax.inject.Inject

class AuthNetworkDataSource @Inject constructor(
    private val authApi: AuthApiService
) {
    suspend fun login(email: String, password: String): TasstyResponse<AuthDto> {
        return safeApiCall2{
            authApi.login(LoginRequest(email, password))
        }
    }

    suspend fun register(email: String, password: String,fullName : String) : TasstyResponse<AuthDto>{
        return safeApiCall2 { authApi.register(RegisterRequest(email,password,fullName)) }
    }

    suspend fun verification(email: String,verificationCode: String) : TasstyResponse<Unit>{
        return safeApiCall2 {
            authApi.verifyEmail(VerifyRequest(email,verificationCode))
        }
    }
}
