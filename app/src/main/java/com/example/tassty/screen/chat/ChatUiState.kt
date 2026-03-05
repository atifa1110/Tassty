package com.example.tassty.screen.chat

import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.NotificationUiModel
import com.example.tassty.ChatTab

data class ChatUiState(
    val chats : Resource<List<ChatUiModel>> = Resource(),
    val notifications : Resource<List<NotificationUiModel>> = Resource(),
    val selectedTab : ChatTab = ChatTab.CHAT
){
    val groupedNotifications: Map<String, List<NotificationUiModel>>
        get() = notifications.data?.groupBy { it.displayHeader } ?: emptyMap()
}