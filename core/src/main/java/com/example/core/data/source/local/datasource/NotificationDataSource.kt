package com.example.core.data.source.local.datasource

import com.example.core.data.source.local.database.dao.NotificationDao
import com.example.core.data.source.local.database.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val notificationDao: NotificationDao
) {

    fun getNotifications(): Flow<List<NotificationEntity>>{
        return notificationDao.getAllNotifications()
    }

    suspend fun insertNotification(notification: NotificationEntity){
        return notificationDao.insertNotification(notification)
    }

    suspend fun markAsRead(id: String){
        return notificationDao.markAsRead(id)
    }

    suspend fun deleteAllNotifications(): Int{
        return notificationDao.deleteAllNotifications()
    }
}