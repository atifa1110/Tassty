package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String,
        image: String,
        token: String?
    ): Flow<TasstyResponse<String>> {
        return chatRepository.connectStreamUser(userId, name, image, token)
    }
}