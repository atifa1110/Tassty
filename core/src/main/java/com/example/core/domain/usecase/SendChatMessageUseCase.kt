package com.example.core.domain.usecase

import android.net.Uri
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(channelId: String, message: String, imageUri: Uri?) : Flow<TasstyResponse<String>> {
        if (message.isBlank() && imageUri == null) {
            return flow {
                emit(TasstyResponse.Error(
                    Meta(code = 400, status = "error", message = "Message or Image cannot be empty")
                ))
            }
        }
        return repository.sendMessage(channelId,message, imageUri)
    }
}