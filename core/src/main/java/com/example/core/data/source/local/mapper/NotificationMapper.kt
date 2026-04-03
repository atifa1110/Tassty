package com.example.core.data.source.local.mapper

import com.example.core.data.source.local.database.entity.NotificationEntity
import com.example.core.domain.model.Notification
import com.example.core.ui.utils.DateFormatter

fun NotificationEntity.toDomain() : Notification{
    return Notification(
        id = this.id,
        relatedId = this.relatedId?:"",
        title = title,
        message = message,
        type = this.type,
        isRead = this.isRead,
        createdAt = DateFormatter.fromTimestamp(this.createdAt)
    )
}

fun Notification.toDatabase() : NotificationEntity {
    return NotificationEntity(
        relatedId = "",
        title = this.title,
        message = this.message,
        type = this.type
    )
}