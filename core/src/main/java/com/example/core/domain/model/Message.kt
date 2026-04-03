package com.example.core.domain.model

import org.threeten.bp.LocalDateTime

data class Message(
    val id: String,
    val text: String,
    val senderName: String?,
    val senderImage: String?,
    val createdAt: LocalDateTime?,
    val createdLocallyAt: LocalDateTime?,
    val isMine: Boolean,
    val isSeen: Boolean,
    val imageAttachment: String?
)
