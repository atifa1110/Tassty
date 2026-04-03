package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotification() : Flow<TasstyResponse<List<Notification>>>

    suspend fun insertNotification(id: String, title: String, message: String, type: String)

    suspend fun markAsRead(id: String)

    suspend fun deleteAllNotifications(): Int
}