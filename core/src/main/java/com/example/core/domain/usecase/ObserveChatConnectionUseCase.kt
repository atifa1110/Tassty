package com.example.core.domain.usecase

import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatConnectionUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isChatReady()
}