package com.example.core.domain.usecase

import com.example.core.data.model.AuthStatus
import com.example.core.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(transform: (AuthStatus) -> AuthStatus) {
        authRepository.updateAuthStatus(transform)
    }
}
