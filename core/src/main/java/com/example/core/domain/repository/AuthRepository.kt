package com.example.core.domain.repository

import com.example.core.data.model.AuthDto
import com.example.core.data.model.AuthStatus
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AuthUser
import com.example.core.domain.model.OtpTimer
import com.example.core.domain.model.UserAddress
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authStatus: Flow<AuthStatus>

    suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus)
    fun register(email: String, password: String,fullName: String, role: String): Flow<TasstyResponse<OtpTimer>>
    fun verifyEmailOtp(email: String, verifyCode: String): Flow<TasstyResponse<String>>
    fun resendEmailOtp(email: String): Flow<TasstyResponse<String>>
    fun setupAccount(address: UserAddress, cuisines: List<String>): Flow<TasstyResponse<String>>
    fun login(email: String, password: String): Flow<TasstyResponse<String>>
    fun refreshToken(): Flow<TasstyResponse<String>>
    fun forgotPassword(email: String): Flow<TasstyResponse<OtpTimer>>
    fun verifyResetOtp(email: String,code: String): Flow<TasstyResponse<String>>
    fun resetPassword(newPassword: String): Flow<TasstyResponse<String>>
    suspend fun logout(): Flow<TasstyResponse<String>>
}