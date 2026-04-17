package com.example.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.ChatStreamDataSource
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Chat
import com.example.core.domain.model.Message
import com.example.core.domain.repository.ChatRepository
import com.example.core.utils.toFile
import dagger.hilt.android.qualifiers.ApplicationContext
import io.getstream.chat.android.models.Attachment
import io.getstream.chat.android.models.Channel
import io.getstream.result.Result
import io.getstream.chat.android.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataSource: ChatStreamDataSource
) : ChatRepository {

    override fun isChatReady(): Flow<Boolean> {
        return dataSource.observeConnectionState()
            .flowOn(Dispatchers.IO)
    }

    override fun getCurrentUserId(): String {
        return dataSource.getCurrentUserId()
    }

    override suspend fun connectStreamUser(
        userId: String,
        userName: String,
        image: String,
        token: String?,
        userRole:String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        if (token.isNullOrEmpty()) {
            emit(TasstyResponse.Error(Meta(code = 0, status = "", message = "Stream Token is missing")))
            return@flow
        }
        val user = User(id = userId, name = userName, image = image,extraData = mutableMapOf("user_role" to userRole))
        val result = dataSource.connectStreamUser(user, token)

        if (result.isSuccess) {
            emit(TasstyResponse.Success("Connect Stream Success", Meta(code = 200, status = "", message = "Chat Connected")))
        } else {
            emit(TasstyResponse.Error(Meta(code = 0, status = "", message = result.errorOrNull()?.message ?: "Connect Stream Failed")))
        }
    }.catch { e ->
        emit(TasstyResponse.Error(Meta(code = 0, status = "", message =  e.message ?: "System Error")))
    }

    override suspend fun sendMessage(
        channelId: String,
        text: String,
        imageUri: Uri?
    ): Flow<TasstyResponse<String>> = channelFlow {
        send(TasstyResponse.Loading())

        val attachments = mutableListOf<Attachment>()

        if (imageUri != null) {
            val file = imageUri.toFile(context)
            if (file == null) {
                send(TasstyResponse.Error(Meta(0, "", "File tidak ditemukan")))
                return@channelFlow
            }

            val uploadResult = dataSource.uploadFile(channelId, file) { progressValue ->
                trySend(TasstyResponse.Loading(progress = progressValue))
            }

            if (uploadResult is Result.Success) {
                attachments.add(Attachment(type = "image", imageUrl = uploadResult.value))
            } else if (uploadResult is Result.Failure) {
                send(TasstyResponse.Error(Meta(400, "error", uploadResult.value.message)))
                return@channelFlow
            }
        }

        val sendResult = dataSource.sendMessage(channelId, text, attachments)
        if (sendResult is Result.Success) {
            send(TasstyResponse.Success(
                data = sendResult.value,
                meta = Meta(200, "success", sendResult.value)
            ))
        } else if (sendResult is Result.Failure) {
            send(TasstyResponse.Error(Meta(400, "error", sendResult.value.message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun getChatChannels(): Flow<TasstyResponse<List<Chat>>> {
        val currentUserId = dataSource.getCurrentUserId()
        return dataSource.watchChannels()
            .map<List<Channel>, TasstyResponse<List<Chat>>> { channels ->
                val domainChannels = channels.map { it.toDomain(currentUserId = currentUserId) }
                TasstyResponse.Success(
                    data = domainChannels,
                    meta = Meta(code = 200, status = "Success", message = "Channels Fetched")
                )
            }
            .onStart {
                emit(TasstyResponse.Loading())
            }
            .catch { e ->
                emit(TasstyResponse.Error(
                    meta = Meta(code = 500, status = "error", message = e.message ?: "Fetch Error")
                ))
            }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteChannel(channelId: String): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.deleteChannel(channelId)

        if (result.isSuccess) {
            emit(
                TasstyResponse.Success(
                data = "Success Delete Channel",
                meta = Meta(code = 200, status = "Success", message = "Success Delete Channel")
            ))
        } else {
            val errorMsg = result.errorOrNull()?.message ?: "Failed Delete Channel"
            emit(TasstyResponse.Error(
                meta = Meta(code = 500, status = "error", message = errorMsg)
            ))
        }
    }.flowOn(Dispatchers.IO)

    override fun getChatMessages(channelId: String): Flow<TasstyResponse<List<Message>>> {
        val currentUserId = dataSource.getCurrentUserId()

        val TAG = "ChatFlowLog"
        return dataSource.watchMessages(channelId)
            .map<List<io.getstream.chat.android.models.Message>, TasstyResponse<List<Message>>> { messages ->
                val filteredMessages = messages.filterNot { streamMsg ->
                    val isSystem = streamMsg.id.contains("stream-system-user")
                    val text = streamMsg.text.lowercase()

                    isSystem && (text.contains("pesanan #") || text.contains("total: rp"))
                }

                val domainData = filteredMessages.map { streamMsg ->
                    streamMsg.toDomain(currentUserId)
                }

                Log.d(TAG, "Berhasil mapping ke Domain. Total: ${domainData.size} pesan")

                TasstyResponse.Success(
                    domainData,
                    Meta(200, "Success", "Message Success Retrieve")
                )
            }
            .onStart {
                Log.d(TAG, "0. Memulai Flow getChatMessages untuk channel: $channelId")
                emit(TasstyResponse.Loading())
            }
            .catch { e ->
                Log.e(TAG, "X. Terjadi Error: ${e.message}")
                emit(TasstyResponse.Error(
                    meta = Meta(code = 500, status = "error", message = e.message ?: "Fetch Error")
                ))
            }.flowOn(Dispatchers.IO)
    }

//    override fun getChatMessages(channelId: String): Flow<TasstyResponse<List<Message>>> {
//        val currentUserId = dataSource.getCurrentUserId()
//
//        val combinedFlow: Flow<TasstyResponse<List<Message>>> = combine(
//            dataSource.watchMessages(channelId),
//
//            // 🔥 FIX DI SINI
//            dataSource.watchChannel(channelId)
//                .onStart { emit(Channel()) }
//
//        ) { messages, channel ->
//
//            val lastReadDate = channel.read
//                .find { it.user.id != currentUserId }
//                ?.lastRead
//
//            val filteredMessages = messages.filterNot { streamMsg ->
//                val isSystem = streamMsg.user.id.contains("stream-system-user")
//                val text = streamMsg.text.lowercase()
//
//                isSystem && (text.contains("pesanan #") || text.contains("total: rp"))
//            }
//
//            val domainData = filteredMessages.map { streamMsg ->
//                val isMyMessage = streamMsg.user.id == currentUserId
//                val isReadByOther = lastReadDate != null
//                val sentBeforeLastRead =
//                    !(streamMsg.createdAt?.after(lastReadDate) ?: true)
//
//                streamMsg.toDomain(currentUserId).copy(
//                    isSeen = isMyMessage && isReadByOther && sentBeforeLastRead
//                )
//            }
//
//            TasstyResponse.Success(
//                domainData,
//                Meta(200, "Success", "Message Success Retrieve")
//            )
//        }
//
//        return combinedFlow
//            .onStart { emit(TasstyResponse.Loading()) }
//            .catch { e ->
//                emit(TasstyResponse.Error(
//                    Meta(400, "Error", e.message ?: "Unknown Error")
//                ))
//            }
//            .flowOn(Dispatchers.Default)
//    }

    override fun getOtherUser(channelId: String): Flow<User>{
        return dataSource.watchOtherUser(channelId).flowOn(Dispatchers.IO)
    }
}
