package com.example.core.ui.mapper

import com.example.core.domain.model.Notification
import com.example.core.ui.model.NotificationType
import com.example.core.ui.model.NotificationUiModel
import com.example.core.utils.DateFormatter

fun Notification.toUiModel() : NotificationUiModel{
    return NotificationUiModel(
        id = this.id,
        title = this.title,
        message = this.message,
        displayTime = DateFormatter.formatMessageTime(this.createdAt),
        displayDate = DateFormatter.formatMessageDate(this.createdAt),
        isRead = this.isRead,
        type = when(this.type){
            "order" -> NotificationType.ORDER
            else -> NotificationType.TRANSACTION
        }
    )
}