package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteChatChannelUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(channelId: String) : Flow<TasstyResponse<String>>{
        return repository.deleteChannel(channelId)
    }
}