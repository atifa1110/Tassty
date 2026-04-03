package com.example.core.domain.usecase

import com.example.core.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
){
    suspend operator fun invoke(){
        return repository.logout()
    }
}