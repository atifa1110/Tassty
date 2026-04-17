package com.example.core.domain.repository

import android.net.Uri
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Chat
import com.example.core.domain.model.Message
import io.getstream.chat.android.models.User
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun isChatReady(): Flow<Boolean>
    fun getCurrentUserId(): String
    suspend fun connectStreamUser(
        userId: String,
        userName: String,
        image: String,
        token: String?,
        userRole:String
    ): Flow<TasstyResponse<String>>
    suspend fun sendMessage(channelId: String, text: String, imageUri: Uri?): Flow<TasstyResponse<String>>
    fun getChatChannels(): Flow<TasstyResponse<List<Chat>>>
    suspend fun deleteChannel(channelId: String): Flow<TasstyResponse<String>>
    fun getChatMessages(channelId: String): Flow<TasstyResponse<List<Message>>>
    fun getOtherUser(channelId: String): Flow<User>

}