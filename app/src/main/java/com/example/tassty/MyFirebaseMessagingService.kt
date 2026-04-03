package com.example.tassty

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.core.data.source.remote.datasource.ChatStreamDataSource
import com.example.core.domain.model.Notification
import com.example.core.domain.repository.NotificationRepository
import com.example.core.domain.repository.OrderRepository
import com.example.tassty.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.android.push.firebase.FirebaseMessagingDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNullOrBlank

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "TasstyPush"

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    @Inject
    lateinit var chatStreamDataSource: ChatStreamDataSource

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessagingDelegate.registerFirebaseToken(token, "Firebase_Tassty")
        CoroutineScope(Dispatchers.IO).launch {
            chatStreamDataSource.syncTokenWithStream(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        Log.d(TAG, "Payload Masuk: $data")
        val notifType = data["notif_type"]

        if (!notifType.isNullOrBlank() && notifType != "chat") {
            val title = data["notif_title"] ?: "Tassty"
            val body = data["notif_message"] ?: ""
            val id = data["related_id"]?: "Empty id"

            if(notifType == "order"){
                orderRepository.triggerOrderUpdate(id)
            }

            showOrderNotification(title, body)
            saveToRoom(data)
        } else {
            val senderName = data["sender_name"] ?: "Pesan Baru"
            val text = data["notif_message"] ?: ""

            if(!isAppInForeground(this)) {
                showChatNotification(
                    senderName = senderName,
                    message = text
                )
            }
        }
    }

    private fun showChatNotification(senderName: String, message: String) {
        val channelId = "chat_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java).apply {
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bitmapLogo = BitmapFactory.decodeResource(resources, R.drawable.logo)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setLargeIcon(bitmapLogo)
            .setContentTitle(senderName)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun showOrderNotification(title: String, message: String) {
        val channelId = "order_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java).apply {
           Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bitmapLogo = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setLargeIcon(bitmapLogo)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        val packageName = context.packageName

        for (process in appProcesses) {
            if (process.importance ==
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                process.processName == packageName
            ) {
                return true
            }
        }
        return false
    }

    private fun saveToRoom(data: Map<String, String>) {
        val title = data["notif_title"] ?: data["sender_name"] ?: "Tassty Update"
        val message = data["notif_message"] ?: ""
        val type = data["notif_type"] ?: "chat"
        val relatedId = data["related_id"]?: data["sender_id"] ?: "Tassty Update"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationRepository.insertNotification(relatedId,title,message,type)
                Log.d(TAG, "Berhasil simpan ke Room: $title")
            } catch (e: Exception) {
                Log.e(TAG, "Gagal simpan ke Room: ${e.message}")
            }
        }
    }
}