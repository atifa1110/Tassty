package com.example.core.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.ConnectChatToStreamUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Foreground Service responsible for maintaining a persistent connection to the Stream Chat server.
 * * It ensures the user remains 'Online' even when the app is in the background or after an app rebuild.
 * This service orchestrates the authentication flow with Stream and synchronizes push notification tokens.
 */
@AndroidEntryPoint
class ChatService : Service() {

    private val tag = "TASSTY_DEBUG"
    @Inject
    lateinit var connectChatUseCase: ConnectChatToStreamUseCase

    @Inject
    lateinit var authDataStore: AuthDataStore

    /**
     * CoroutineScope bound to the service lifecycle using SupervisorJob to ensure
     * that failures in connection attempts do not cancel the entire service scope.
     */
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    /**
     * Triggered when the service is started via startService or startForegroundService.
     * It handles the core logic of retrieving session data and initiating the connection.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Required for Foreground Services to avoid being killed by the OS
        showNotification()
        Log.d(tag, "ChatService: onStartCommand dijalankan")

        serviceScope.launch {
            val authStatus = authDataStore.authStatus.first()

            Log.d(tag, "ChatService: Mencoba konek untuk User: ${authStatus.userId}")
            // Retrieve the latest authentication state from DataStore
            if (authStatus.isLoggedIn && !authStatus.streamToken.isNullOrEmpty()) {
                // Execute the domain logic to establish Stream connection
                connectChatUseCase(
                    userId = authStatus.userId?:"",
                    name = authStatus.name?:"",
                    image = authStatus.profileImage?:"",
                    token = authStatus.streamToken,
                    userRole = authStatus.role?:""
                ).collect { result ->
                    when (result) {
                        is TasstyResponse.Success -> Log.d(tag, "Stream Connected! Role: ${authStatus.role}")
                        is TasstyResponse.Error -> Log.e(
                            tag,
                            "Connection Failed: ${result.meta.message}"
                        )

                        else -> Log.d(tag, "ChatService: Mencoba konek untuk User: ${authStatus.userId}")
                    }
                }
            }else{
                // Terminate service if no valid session is found
                stopSelf()
            }
        }

        // start sticky ensures the OS restarts the service if it's killed due to low memory
        return START_STICKY
    }


    /**
     * Creates and displays a persistent notification required for Foreground Services.
     * For Android O and above, it also manages the Notification Channel.
     */
    private fun showNotification() {
        val channelId = "chat_sync_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Chat Service", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tassty Chat")
            .setContentText("Menghubungkan ke server...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /**
     * Ensures all active coroutines are cancelled when the service is destroyed
     * to prevent memory leaks and orphaned background tasks.
     */
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}