package com.example.core.domain.model

import com.example.core.ui.model.SenderType
import org.threeten.bp.LocalDateTime
import java.util.Date

data class Chat(
    val id: String,
    val senderName: String,
    val lastMessage: String,
    val profileImage: String,
    val unreadCount: Int,
    val isSeen: Boolean,
    val isOnline: Boolean,
    val lastMessageAt: LocalDateTime?,
    val senderType: SenderType,
)
