package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.CardUser
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserCardUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() : Flow<TasstyResponse<List<CardUser>>>{
        return userRepository.getUserCard()
    }
}