package com.example.core.domain.usecase

import com.example.core.domain.repository.ChatRepository
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatOtherUserUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(channelId: String): Flow<User>{
        return repository.getOtherUser(channelId)
    }
}