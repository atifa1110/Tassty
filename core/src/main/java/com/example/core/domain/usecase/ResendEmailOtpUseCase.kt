package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResendEmailOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke(email: String) : Flow<TasstyResponse<String>> {
        return authRepository.resendEmailOtp(email)
    }
}