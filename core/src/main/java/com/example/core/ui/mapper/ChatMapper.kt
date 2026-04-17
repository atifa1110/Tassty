package com.example.core.ui.mapper

import com.example.core.utils.DateFormatter
import com.example.core.domain.model.Chat
import com.example.core.ui.model.ChatUiModel

fun Chat.toUiModel() : ChatUiModel {
    return ChatUiModel(
        id = this.id,
        senderName = this.senderName,
        lastMessage= this.lastMessage,
        profileImage = this.profileImage,
        unreadCount = this.unreadCount,
        isOnline = this.isOnline,
        displayTime = DateFormatter.formatChatTime(this.lastMessageAt),
        senderType = this.senderType,
        isSwipeActionVisible = false
    )
}
