package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.model.AuthStatus
import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.data.source.remote.datasource.AuthNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AuthUser
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authDataStore: AuthDataStore,
) : AuthRepository{

    override val authStatus: Flow<AuthStatus> = authDataStore.authStatus
    override suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus) {
        authDataStore.updateAuthStatus(transform)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Flow<TasstyResponse<AuthUser>> = flow {
        emit(TasstyResponse.Loading)
        val response = authNetworkDataSource.login(email,password)
        when(response) {
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        fullName: String
    ): Flow<TasstyResponse<AuthUser>> = flow {
        emit(TasstyResponse.Loading)
        val response = authNetworkDataSource.register(email, password, fullName)
        when (response) {
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }

    override suspend fun verify(
        email: String,
        verifyCode: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading)
        val response = authNetworkDataSource.verification(email,verifyCode)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }
}