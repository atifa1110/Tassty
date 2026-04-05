package com.example.core.data.source.remote.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.ui.utils.toFile
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.events.ChannelDeletedEvent
import io.getstream.chat.android.client.events.MessageReadEvent
import io.getstream.chat.android.client.events.NewMessageEvent
import io.getstream.chat.android.client.events.NotificationAddedToChannelEvent
import io.getstream.chat.android.client.events.NotificationChannelDeletedEvent
import io.getstream.chat.android.client.events.NotificationMarkReadEvent
import io.getstream.chat.android.client.events.UserPresenceChangedEvent
import io.getstream.chat.android.client.events.UserUpdatedEvent
import io.getstream.chat.android.client.utils.ProgressCallback
import io.getstream.chat.android.models.Attachment
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.ConnectionData
import io.getstream.chat.android.models.Device
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.models.PushProvider
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.querysort.QuerySortByField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import io.getstream.result.Result
import io.getstream.result.Error
import java.io.File
import javax.inject.Inject

class ChatStreamDataSource @Inject constructor(
    private val chatClient: ChatClient,
    private val authDataStore: AuthDataStore
) {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val tag = "WATCH_CHAT_STREAM"

    fun getCurrentUserId(): String {
        return chatClient.getCurrentUser()?.id ?: ""
    }

    suspend fun connectStreamUser(
        user: User,
        token: String
    ): Result<ConnectionData> {
        val result = chatClient.connectUser(user, token).await()

        if (result.isSuccess) {
            val status = authDataStore.authStatus.first()
            val firebaseToken = status.firebaseToken?:""

            if (firebaseToken.isEmpty()) {
                fetchTokenSync()
            }
        }

        return result
    }

    fun fetchTokenSync() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { syncTokenWithStream(it) }
            }
        }
    }

    fun syncTokenWithStream(newToken: String) {
        scope.launch {
            val status = authDataStore.authStatus.first()
            val savedToken = status.firebaseToken

            if (newToken == savedToken) return@launch

            val devicesResult = chatClient.getDevices().await()
            if(devicesResult.isSuccess){
                val isAlreadyRegistered = devicesResult.getOrNull()?.any { it.token == newToken } ?: false

                if(!isAlreadyRegistered){
                    val device = Device(
                        token = newToken,
                        pushProvider = PushProvider.FIREBASE,
                        providerName = "Firebase_Tassty"
                    )
                    val addResult = chatClient.addDevice(device).await()
                    if (addResult.isSuccess) {
                        authDataStore.updateAuthStatus { it.copy(firebaseToken = newToken) }
                        Log.d(tag, "Token baru berhasil didaftarkan.")
                    }else{
                        Log.d(tag, "Token baru gagal didaftarkan.")
                    }
                } else {
                    authDataStore.updateAuthStatus { it.copy(firebaseToken = newToken) }
                }
            }
        }
    }

    suspend fun uploadFile(
        channelId: String,
        file: File,
        onProgress: (Float) -> Unit
    ): Result<String> {
        val channelClient = chatClient.channel(channelId)

        val result = channelClient.sendImage(
            file = file,
            callback = object : ProgressCallback {
                override fun onSuccess(url: String?) {}
                override fun onError(error: Error) {}
                override fun onProgress(bytesUploaded: Long, totalBytes: Long) {
                    if (totalBytes > 0) {
                        val progress = bytesUploaded.toFloat() / totalBytes.toFloat()
                        onProgress(progress)
                    }
                }
            }
        ).await()

        return if (result.isSuccess) {
            val url = result.getOrNull()?.file
            if (url != null) Result.Success(url)
            else Result.Failure(Error.GenericError("URL null"))
        } else {
            Result.Failure(result.errorOrNull() ?: Error.GenericError("Upload failed"))
        }
    }

    suspend fun sendMessage(
        channelId: String,
        text: String,
        attachments: List<Attachment>
    ): Result<String> {

        val channelClient = chatClient.channel(channelId)

        val pushBody = when {
            text.isNotEmpty() -> text
            attachments.isNotEmpty() -> "Sent an image"
            else -> "New message"
        }

        val extraData = mutableMapOf(
            "notif_type" to "chat",
            "notif_message" to pushBody,
            "push" to mapOf(
                "title" to "Tassty",
                "body" to pushBody
            )
        )

        val message = Message(
            text = text,
            cid = channelId,
            attachments = attachments,
            extraData = extraData
        )

        val result = channelClient.sendMessage(message).await()

        return if (result.isSuccess) {
            Result.Success("Send Message Success")
        } else {
            Result.Failure(result.errorOrNull() ?: Error.GenericError("Unknown Error"))
        }
    }

    fun watchChannels(): Flow<List<Channel>> = callbackFlow {
        val userId = chatClient.getCurrentUser()?.id
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val filter = Filters.`in`("members", listOf(userId))

        val request = QueryChannelsRequest(
            filter = filter,
            querySort = QuerySortByField.descByName("last_message_at"),
            limit = 30
        ).apply {
            watch = true
            state = true
            messageLimit = 1
            memberLimit = 2
        }

        val result = chatClient.queryChannels(request).execute()
        trySend(result.getOrNull() ?: emptyList())

        val subscription = chatClient.subscribeFor(
            NewMessageEvent::class.java,
            NotificationChannelDeletedEvent::class.java,
            ChannelDeletedEvent::class.java,
            NotificationAddedToChannelEvent::class.java
        ) {
            val updated = chatClient
                .queryChannels(request)
                .execute()
                .getOrNull() ?: emptyList()

            trySend(updated)
        }

        awaitClose {
            subscription.dispose()
        }
    }

    fun watchChannel(channelId: String): Flow<Channel> = callbackFlow {
        val channelClient = chatClient.channel(channelId)

        val initialRequest = QueryChannelRequest().withState()
        val result = channelClient.query(initialRequest).await()

        if (result.isSuccess) {
            result.getOrNull()?.let { trySend(it) }
        } else {
            close(Exception(result.errorOrNull()?.message ?: "Gagal memuat channel"))
        }

        val subscription = channelClient.subscribe { event ->
            scope.launch {
                val updatedResult = channelClient.query(QueryChannelRequest().withState()).await()
                updatedResult.getOrNull()?.let { trySend(it) }
            }
        }

        awaitClose {
            subscription.dispose()
        }
    }

    suspend fun deleteChannel(channelId: String): Result<Channel> {
        val channelClient = chatClient.channel(channelId)
        return channelClient.delete().await()
    }

    fun watchMessages(channelId: String): Flow<List<Message>> = callbackFlow {
        val channelClient = chatClient.channel(channelId)
        channelClient.markRead().execute()

        // Get Messages
        val initialResult = channelClient.query(QueryChannelRequest().withMessages(30)).execute()

        if (initialResult.isSuccess) {
            val messageList = initialResult.getOrNull()?.messages?.toMutableList() ?: mutableListOf()
            trySend(messageList.toList())

            // Subscribe new message
            val subscription = channelClient.subscribe { event ->
                when(event){
                    is NewMessageEvent -> {
                        val newMessage = event.message
                        messageList.add(newMessage)
                        trySend(messageList.toList())
                        channelClient.markRead().enqueue()
                    }

                    is MessageReadEvent -> {
                        trySend(messageList.toList())
                    }

                    is NotificationMarkReadEvent -> {
                        trySend(messageList.toList())
                    }

                    else -> Unit
                }
            }

            awaitClose { subscription.dispose() }
        } else {
            val errorMsg = initialResult.errorOrNull()?.message ?: "Gagal memuat pesan"
            close(Exception(errorMsg))
        }
    }

    fun watchOtherUser(channelId: String): Flow<User> = callbackFlow {
        val currentUserId = chatClient.getCurrentUser()?.id

        if (currentUserId == null) {
            Log.e(tag, "Client belum login! Pastikan setUser dipanggil sebelum masuk sini.")
            trySend(User(name = "Not Logged In"))
            close()
            return@callbackFlow
        }

        val channelClient = chatClient.channel(channelId)

        // get info user
        val initialQuery = channelClient.query(QueryChannelRequest()).await()
        if (initialQuery.isSuccess) {
            val otherMember = initialQuery.getOrNull()?.members?.find { it.user.id != currentUserId }?.user
            Log.d(tag, "Berhasil! User: ${otherMember?.name}, Role: ${otherMember?.role}")
            otherMember?.let { trySend(it) }
        }

        // Subscribe status changes (Online/Offline) or other profile update
        val disposable = channelClient.subscribe { event ->
            if (event is UserPresenceChangedEvent || event is UserUpdatedEvent) {
                launch {
                    val updatedResult = channelClient.query(QueryChannelRequest()).await()
                    val updatedUser = updatedResult.getOrNull()?.members?.find { it.user.id != currentUserId }?.user
                    if (updatedUser != null) trySend(updatedUser)
                }
            }
        }
        awaitClose { disposable.dispose() }
    }

    suspend fun removeAllDevices(): Result<Unit> = try {
        val result = chatClient.getDevices().await()

        if (result.isSuccess) {
            val devices = result.getOrNull() ?: emptyList()
            devices.forEach { device ->
                val deleteResult = chatClient.deleteDevice(device).await()

                if (deleteResult.isFailure) {
                    val error = deleteResult.errorOrNull()
                    Log.e(tag, "Gagal hapus device ${device.token}: ${error?.message}")
                }
            }
            Result.Success(Unit)
        } else {
            Result.Success(Unit)
        }
    } catch (e: Exception) {
        Log.e(tag, "Exception di removeAllDevices: ${e.message}")
        Result.Success(Unit)
    }

    suspend fun disconnect() {
        try {
            chatClient.disconnect(flushPersistence = true).await()
            Log.d(tag, "Stream Chat disconnected & persistence flushed")
        } catch (e: Exception) {
            Log.e(tag, "Gagal disconnect chat: ${e.message}")
        }
    }
}