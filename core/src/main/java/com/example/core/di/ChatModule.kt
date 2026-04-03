package com.example.core.di

import android.content.Context
import com.example.core.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.notifications.handler.NotificationConfig
import io.getstream.android.push.firebase.FirebasePushDeviceGenerator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    @Singleton
    fun provideChatClient(@ApplicationContext context: Context): ChatClient {

        val notificationConfig = NotificationConfig(
            pushDeviceGenerators = listOf(
                FirebasePushDeviceGenerator(providerName = "Firebase_Tassty", context = context)
            ),
        )

        return ChatClient.Builder(BuildConfig.STREAM_API_KEY, context)
            .notifications(notificationConfig)
            .logLevel(ChatLogLevel.ALL)
            .build()
    }
}