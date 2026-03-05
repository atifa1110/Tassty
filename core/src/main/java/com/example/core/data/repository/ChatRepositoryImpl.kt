package com.example.core.data.repository

import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.ChatRepository
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatClient: ChatClient
) : ChatRepository {

    override suspend fun connectStreamUser(
        userId: String,
        userName: String,
        image: String,
        token: String?
    ): Flow<TasstyResponse<String>> = callbackFlow {
        trySend(TasstyResponse.Loading)

        if (token.isNullOrEmpty()) {
            trySend(TasstyResponse.Error(Meta(0, status = "", "Token Stream tidak ditemukan")))
            close()
            return@callbackFlow
        }

        val user = User(id = userId, name = userName, image = image)
        chatClient.connectUser(user, token).enqueue { result ->
            if (result.isSuccess) {
                trySend(
                    TasstyResponse.Success(
                        meta = Meta(200, status = "", message = "Connected to Stream"),
                        data = "Stream Chat Connected Successfully" // Return String
                    )
                )
            } else {
                trySend(
                    TasstyResponse.Error(
                        Meta(
                            0,
                            status = "",
                            "Failed connect to stream: ${result.errorOrNull()?.message}"
                        )
                    )
                )
            }
            close()
        }

        awaitClose { /* Cleanup */ }
    }
}