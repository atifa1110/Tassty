package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserCardUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke(cardId: String): Flow<TasstyResponse<String>>{
        return userRepository.deleteUserCard(cardId)
    }
}