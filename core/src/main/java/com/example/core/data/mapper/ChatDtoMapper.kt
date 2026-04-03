package com.example.core.data.mapper

import com.example.core.domain.model.Chat
import com.example.core.ui.model.SenderType
import com.example.core.ui.utils.DateFormatter
import io.getstream.chat.android.client.extensions.currentUserUnreadCount
import io.getstream.chat.android.models.Channel

fun Channel.toDomain(currentUserId: String): Chat{
    val lastMsg = this.messages.lastOrNull()
    val displayMessage = when {
        lastMsg?.text?.isNotBlank() == true -> lastMsg.text
        lastMsg?.attachments?.any { it.type == "image" } == true -> "Sent an image"
        lastMsg?.attachments?.isNotEmpty() == true -> "Sent an attachment"
        else -> "No messages"
    }
    val otherMember = this.members.find { it.user.id != currentUserId }
    val otherMemberRead = this.read.find { it.user.id != currentUserId }

    val lastReadDate = otherMemberRead?.lastRead
    val lastMsgAt = lastMsg?.createdAt

    val unread = this.currentUserUnreadCount(currentUserId)
    val customRole = otherMember?.user?.extraData?.get("user_role") as? String ?: "user"
    return Chat(
        id = this.cid,
        senderName = otherMember?.user?.name?: this.name,
        lastMessage= displayMessage,
        profileImage = otherMember?.user?.image?:"",
        unreadCount = unread,
        isSeen = if (lastReadDate != null && lastMsgAt != null) !lastMsgAt.after(lastReadDate) else false,
        isOnline = otherMember?.user?.online?:false,
        lastMessageAt = DateFormatter.fromDate(this.lastMessageAt),
        senderType = if (customRole.lowercase() == "driver") SenderType.DRIVER else SenderType.USER
    )
}