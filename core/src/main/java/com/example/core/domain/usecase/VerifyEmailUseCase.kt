package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, verifyCode: String) : Flow<TasstyResponse<String>> =
        flow {
            authRepository.verify(email,verifyCode).collect { result ->
                when (result) {
                    is TasstyResponse.Success -> {
                        authRepository.updateAuthStatus { current ->
                            current.copy(
                                isVerified = true
                            )
                        }
                        emit(result) // pass success downstream
                    }

                    is TasstyResponse.Error -> {
                        emit(result) // just pass error downstream
                    }

                    is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
                }
            }
        }
}