package com.example.core.data.repository

import android.util.Log
import com.example.core.data.mapper.toDomainOtp
import com.example.core.data.model.AuthStatus
import com.example.core.data.model.RegistrationStep
import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.data.source.remote.datasource.AuthNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.OtpTimer
import com.example.core.domain.model.UserAddress
import com.example.core.domain.repository.AuthRepository
import com.example.core.ui.mapper.toRequestDto
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val chatClient: ChatClient,
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authDataStore: AuthDataStore,
) : AuthRepository{

    val tag = "AuthRepositoryImpl"
    override val authStatus: Flow<AuthStatus> = authDataStore.authStatus

    override suspend fun updateAuthStatus(transform: (AuthStatus) -> AuthStatus) {
        authDataStore.updateAuthStatus(transform)
    }

    override fun register(
        email: String,
        password: String,
        fullName: String,
        role: String
    ): Flow<TasstyResponse<OtpTimer>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.register(email, password, fullName, role)
        when (response) {
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                val authData = response.data
                if (authData != null) {
                    authDataStore.updateAuthStatus { current ->
                        current.copy(
                            registrationStep = RegistrationStep.AWAITING_CONFIRMATION,
                            userId = authData.userId,
                            email = authData.email,
                            name = authData.name,
                            role = authData.role
                        )
                    }
                }
                emit(TasstyResponse.Success(response.data?.toDomainOtp(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyEmailOtp(
        email: String,
        verifyCode: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.verifyEmailOtp(email,verifyCode)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                val authData = response.data

                if (authData != null) {
                    authDataStore.updateAuthStatus { current ->
                        current.copy(
                            registrationStep = RegistrationStep.VERIFIED,
                            isVerified = true,
                            accessToken = authData.accessToken,
                            refreshToken = authData.refreshToken,
                        )
                    }
                }
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun resendEmailOtp(email: String): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.resendEmailOtp(email)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun setupAccount(address: UserAddress, cuisines: List<String>): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val request = address.toRequestDto(cuisines)
        val response = authNetworkDataSource.setupAccount(request)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                authDataStore.updateAuthStatus { current ->
                    current.copy(
                        hasCompletedSetup = true,
                        addressName = request.addressName
                    )
                }
                emit(TasstyResponse.Success(response.meta.message,response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun login(
        email: String,
        password: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.login(email,password)
        when(response) {
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                val authData = response.data
                if (authData != null) {
                    authDataStore.updateAuthStatus { current ->
                        current.copy(
                            isLoggedIn = true,
                            userId = authData.userId,
                            name = authData.name,
                            role = authData.role,
                            profileImage = authData.profileImage,
                            addressName = authData.addressName,
                            accessToken = authData.accessToken,
                            streamToken = authData.streamToken,
                            refreshToken = authData.refreshToken
                        )
                    }
                }
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun refreshToken(refreshToken: String): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.refreshToken(refreshToken)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                val authData = response.data
                authDataStore.updateAuthStatus { current ->
                    current.copy(
                        accessToken = authData?.accessToken,
                        refreshToken = authData?.refreshToken
                    )
                }
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun forgotPassword(email: String): Flow<TasstyResponse<OtpTimer>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.forgotPassword(email)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                authDataStore.updateAuthStatus { current ->
                    current.copy(
                        email = email
                    )
                }
                emit(TasstyResponse.Success(response.data?.toDomainOtp(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyResetOtp(
        email: String,
        code: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.verifyResetOtp(email,code)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                val authData = response.data
                if (authData != null) {
                    authDataStore.updateAuthStatus { current ->
                        current.copy(
                            accessToken = authData.accessToken,
                            refreshToken = authData.refreshToken,
                        )
                    }
                }
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun resetPassword(newPassword: String): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val response = authNetworkDataSource.resetPassword(newPassword)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                authDataStore.updateAuthStatus { current->
                    current.copy(
                        email = "",
                        accessToken = "",
                        refreshToken = ""
                    )
                }
                emit(TasstyResponse.Success(response.meta.message, response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout() {
        try {
            val devicesResult = chatClient.getDevices().await()

            if (devicesResult.isSuccess) {
                val devices = devicesResult.getOrNull()?:emptyList()

                devices.forEach { device ->
                    chatClient.deleteDevice(device).await()
                    Log.d(tag, "Device dengan token ${device.token} berhasil dihapus")
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Gagal mengambil/menghapus list devices: ${e.message}")
        }

        authDataStore.clearAuthStatus()
        chatClient.disconnect(flushPersistence = true).await()
    }
}

