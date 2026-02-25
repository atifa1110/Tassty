package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterEmailPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String, fullName: String,role: String): Flow<TasstyResponse<String>> = flow {
        val result = authRepository.register(email, password, fullName, role)
        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.meta.message,result.meta))
                }
                is TasstyResponse.Error -> { emit(result) }
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}


