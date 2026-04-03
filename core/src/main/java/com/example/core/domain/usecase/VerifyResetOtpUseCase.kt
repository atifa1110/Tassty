package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyResetOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, verifyCode: String): Flow<TasstyResponse<String>> {
        return repository.verifyResetOtp(email,verifyCode)
    }
}