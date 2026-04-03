package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Chat
import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatListChannelUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke() : Flow<TasstyResponse<List<Chat>>> {
        return repository.getChatChannels()
    }
}