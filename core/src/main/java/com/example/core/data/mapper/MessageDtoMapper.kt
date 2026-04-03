package com.example.core.data.mapper

import com.example.core.domain.model.Message
import com.example.core.ui.utils.DateFormatter
import io.getstream.chat.android.models.Message as StreamMessage


fun StreamMessage.toDomain(currentUserId: String): Message {
    val imageAttachment = attachments
        .firstOrNull { it.type == "image" }
        ?.imageUrl

    return Message(
        id = this.id,
        text = this.text,
        senderName = this.user.name,
        senderImage = this.user.image,
        createdAt = DateFormatter.fromDate(this.createdAt),
        createdLocallyAt = DateFormatter.fromDate(this.createdLocallyAt),
        isMine = this.user.id == currentUserId,
        isSeen = false,
        imageAttachment = imageAttachment
    )
}
