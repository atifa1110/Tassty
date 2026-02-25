package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResendVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke(email: String) : Flow<TasstyResponse<String>> = flow {
        val result = authRepository.resend(email)
        result.collect { result->
            when(result){
                is TasstyResponse.Error -> {
                    emit(TasstyResponse.Error(result.meta))
                }
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.meta.message,result.meta))
                }
            }
        }
    }
}