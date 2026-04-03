package com.example.core.domain.usecase

import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetChatCurrentUserIdUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<String> {
        return flowOf(repository.getCurrentUserId())
    }
}