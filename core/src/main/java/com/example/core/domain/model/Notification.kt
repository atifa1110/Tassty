package com.example.core.domain.model

import org.threeten.bp.LocalDateTime

data class Notification(
    val id: String,
    val relatedId: String,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: LocalDateTime
)
