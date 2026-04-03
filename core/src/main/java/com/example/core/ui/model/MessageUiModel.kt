package com.example.core.ui.model

data class MessageUiModel(
    val id: String,
    val text: String,
    val senderName: String,
    val senderImage: String,
    val date: String,
    val time: String,
    val isMine: Boolean,
    val isSeen: Boolean,
    val imageAttachment: String
)
