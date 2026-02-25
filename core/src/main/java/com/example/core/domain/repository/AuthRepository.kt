package com.example.core.domain.repository

import com.example.core.data.model.AuthStatus
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AuthUser
import com.example.core.domain.model.UserAddress
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authStatus: Flow<AuthStatus>

    suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus)
    suspend fun login(email: String, password: String): Flow<TasstyResponse<AuthUser>>
    suspend fun register(email: String, password: String,fullName: String, role: String): Flow<TasstyResponse<AuthUser>>
    suspend fun verify(email: String, verifyCode: String): Flow<TasstyResponse<AuthUser>>
    suspend fun resend(email: String): Flow<TasstyResponse<String>>
    suspend fun setupAccount(address: UserAddress, cuisines: List<String>): Flow<TasstyResponse<String>>
}