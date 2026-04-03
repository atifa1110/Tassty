package com.example.tassty.screen.chat

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.NotificationUiModel
import com.example.tassty.ChatTab
import com.google.common.base.Objects

data class ChatUiState(
    val chats : Resource<List<ChatUiModel>> = Resource(),
    val notifications : Resource<List<NotificationUiModel>> = Resource(),
    val selectedTab : ChatTab = ChatTab.CHAT,
    val revealedChatIds: Set<String> = emptySet(),
    val isDeleteChatSheetVisible: Boolean = false,
    val isDeleteNotifSheetVisible: Boolean = false,
    val selectedChat: ChatUiModel? = null
){
    val groupedNotifications: Map<String, List<NotificationUiModel>>
        get() = notifications.data?.groupBy { it.displayDate } ?: emptyMap()
}

data class ChatInternalState(
    val selectedTab : ChatTab = ChatTab.CHAT,
    val revealedChatIds: Set<String> = emptySet(),
    val isDeleteChatSheetVisible: Boolean = false,
    val isDeleteNotifSheetVisible: Boolean = false,
    val selectedChat: ChatUiModel? = null
)

sealed class ChatEvent{
    data class ShowMessage(val message: String) : ChatEvent()
}