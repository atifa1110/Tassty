package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
){
    operator fun invoke(newPassword: String): Flow<TasstyResponse<String>>{
        return repository.resetPassword(newPassword)
    }

}