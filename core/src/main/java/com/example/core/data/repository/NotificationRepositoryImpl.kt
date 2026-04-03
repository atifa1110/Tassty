package com.example.core.data.repository

import com.example.core.data.source.local.database.entity.NotificationEntity
import com.example.core.data.source.local.datasource.NotificationDataSource
import com.example.core.data.source.local.mapper.toDomain
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Notification
import com.example.core.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dataSource: NotificationDataSource
) : NotificationRepository{

    override fun getNotification(): Flow<TasstyResponse<List<Notification>>> =
        dataSource.getNotifications()
            .map { entities ->
                entities.map { it.toDomain() }
            }
            .map<List<Notification>, TasstyResponse<List<Notification>>> { domainList ->
                TasstyResponse.Success(domainList, Meta(200, "success", "Get Notifications Success"))
            }
            .onStart { emit(TasstyResponse.Loading()) }
            .catch { e ->
                emit(TasstyResponse.Error(Meta(400, "error", e.message ?: "Failed")))
            }

    override suspend fun insertNotification(id: String, title: String, message: String, type: String) {
        val entity = NotificationEntity(relatedId = id, title = title, message = message, type = type,)
        return dataSource.insertNotification(entity)
    }

    override suspend fun markAsRead(id: String) {
        return dataSource.markAsRead(id)
    }

    override suspend fun deleteAllNotifications(): Int {
        return dataSource.deleteAllNotifications()
    }
}