package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginEmailPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) : Flow<TasstyResponse<String>> = flow {
        authRepository.login(email,password).collect { result ->
                when (result) {
                    is TasstyResponse.Success -> {
                        Log.d("LoginUseCase", "SUCCESS DATA = ${result.data}")
                        Log.d("LoginUseCase", "NAME = ${result.data?.name}")

                        authRepository.updateAuthStatus { current ->
                            current.copy(
                                isLoggedIn = true,
                                name = result.data?.name,
                                profileImage = result.data?.profileImage,
                                addressName = result.data?.addressName,
                                accessToken = result.data?.accessToken,
                                refreshToken = result.data?.refreshToken
                            )
                        }
                        emit(TasstyResponse.Success(result.meta.message,result.meta))
                    }

                    is TasstyResponse.Error -> {
                        Log.e("LoginUseCase", "ERROR = ${result.meta.message}")
                        emit(result) // just pass error downstream
                    }

                    is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
                }
            }
        }
}