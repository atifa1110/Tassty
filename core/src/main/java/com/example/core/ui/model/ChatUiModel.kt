package com.example.core.ui.model

data class ChatUiModel(
    val id: String,
    val senderName: String,
    val lastMessage: String,
    val senderProfileImage: String,
    val unreadCount: Int,
    val isOnline: Boolean,
    val senderType: SenderType,
    val displayTime:String
)

enum class SenderType {
    DRIVER, RESTAURANT
}
