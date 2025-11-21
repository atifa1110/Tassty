package com.example.core.domain.usecase

import com.example.core.data.model.AuthStatus
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStatusUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<AuthStatus> = repository.authStatus
}