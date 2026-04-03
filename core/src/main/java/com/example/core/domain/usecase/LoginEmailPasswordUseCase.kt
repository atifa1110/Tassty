package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.AuthUser
import com.example.core.domain.repository.AuthRepository
import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginEmailPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) : Flow<TasstyResponse<String>> {
        return authRepository.login(email,password)
    }
}