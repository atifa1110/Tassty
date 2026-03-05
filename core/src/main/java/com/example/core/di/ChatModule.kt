package com.example.core.di

import android.content.Context
import com.example.core.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    @Singleton
    fun provideChatClient(@ApplicationContext context: Context): ChatClient {
        return ChatClient.Builder(BuildConfig.STREAM_API_KEY, context).build()
    }
}