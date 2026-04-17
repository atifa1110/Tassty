package com.example.core.ui.mapper

import com.example.core.utils.DateFormatter
import com.example.core.domain.model.Message
import com.example.core.ui.model.MessageUiModel
import java.util.Date

fun Message.toUiModel(): MessageUiModel {
    val date = this.createdAt ?: this.createdLocallyAt
    return MessageUiModel(
        id = this.id,
        text = this.text,
        senderName = this.senderName ?: "No Name",
        senderImage = this.senderImage ?: "",
        date = DateFormatter.formatMessageDate(date),
        time = DateFormatter.formatMessageTime(date),
        isMine = this.isMine,
        isSeen = this.isSeen,
        imageAttachment = this.imageAttachment ?: ""
    )
}

