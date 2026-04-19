package com.example.tassty.util

import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.MessageUiModel
import com.example.core.ui.model.NotificationType
import com.example.core.ui.model.NotificationUiModel
import com.example.core.ui.model.SenderType
import okhttp3.internal.toImmutableList

object ChatData {
    val dummyNotifications = listOf(
        NotificationUiModel(
            id = "1",
            title = "Your order has been taken by the driver!",
            message = "Your order on its way and should arrive shortly",
            displayTime = "15.00 PM",
            displayDate = "Today",
            isRead = true,
            type = NotificationType.ORDER
        ),
        NotificationUiModel(
            id = "2",
            title = "45% Special Discount for Tassty Membership!",
            message = "Lorem ipsum dolor sit amet",
            displayTime = "14.25 PM",
            displayDate = "Today",
            isRead = false,
            type = NotificationType.DISCOUNT
        ),
        NotificationUiModel(
            id = "3",
            title = "45% Special Discount for Tassty Membership!",
            message = "Lorem ipsum dolor sit amet",
            displayTime = "14.25 PM",
            displayDate= "Yesterday",
            isRead = false,
            type = NotificationType.DISCOUNT
        )
    ).toImmutableList()

    val dummyChats = listOf(
        ChatUiModel(
            id = "1",
            senderName = "Lucas Nathan",
            lastMessage = "Your order on its way and should arrive shortly",
            profileImage = "",
            displayTime = "27/09/24",
            unreadCount = 2,
            isOnline = true,
            senderType = SenderType.USER,
            isSwipeActionVisible = false
        ),
        ChatUiModel(
            id = "2",
            senderName = "Indah Café",
            lastMessage = "Your order will be prepared and delivered now",
            profileImage = "",
            displayTime = "27/09/24",
            unreadCount = 0,
            isOnline = false,
            senderType = SenderType.RESTAURANT,
            isSwipeActionVisible = false
        )
    ).toImmutableList()

    val dummyMessages = listOf(
        MessageUiModel(
            id = "MES-001",
            text = "I can't seem to find the location",
            senderName = "Henry",
            senderImage = "",
            date = "Today",
            time = "09:35 AM",
            isSeen = true,
            isMine = false,
            imageAttachment = ""
        ) ,
        MessageUiModel(
            id = "MES-002",
            text = "I can't seem to find the location",
            senderName = "Henry",
            senderImage = "",
            date = "Today",
            time = "10:10 AM",
            isSeen = true,
            isMine = true,
            imageAttachment = ""
        ),
    ).toImmutableList()
}