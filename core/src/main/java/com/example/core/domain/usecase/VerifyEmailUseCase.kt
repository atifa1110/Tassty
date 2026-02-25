package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, verifyCode: String): Flow<TasstyResponse<String>> = flow {
        authRepository.verify(email, verifyCode).collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.meta.message, result.meta))
                }

                is TasstyResponse.Error -> emit(TasstyResponse.Error(result.meta))
                is TasstyResponse.Loading -> TasstyResponse.Loading
            }
        }
    }
}