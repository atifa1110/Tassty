package com.example.core.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val relatedId: String?,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)