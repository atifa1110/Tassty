package com.example.tassty.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.NotificationUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.tassty.ChatTab
import com.example.tassty.component.ChatCard
import com.example.tassty.component.ChatTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyChatContent
import com.example.tassty.component.EmptyNotificationContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.HorizontalTitleItemCountButtonSection
import com.example.tassty.component.HorizontalTitleItemCountSection
import com.example.tassty.component.LoadingScreen
import com.example.tassty.component.LocationLardCard
import com.example.tassty.component.NotificationCard
import com.example.tassty.component.OrderListCard
import com.example.tassty.component.addressVerticalListBlock
import com.example.tassty.dummyChats
import com.example.tassty.dummyNotifications
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {

}

@Composable
fun ChatContent(
    uiState: ChatUiState
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            ChatTopAppBar {  }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
                .fillMaxSize()
                .background(Neutral10)
        ) {
            item {
                ChatTabContent(
                    selectedType = uiState.selectedTab,
                    onTabSelected = {}
                )
            }

            when (uiState.selectedTab) {
                ChatTab.CHAT -> {
                    chatListContent(resource = uiState.chats, onClick = {})
                }
                ChatTab.NOTIFICATION -> {
                    notificationsListContent(
                        resource = uiState.notifications,
                        groupNotifications = uiState.groupedNotifications
                    )
                }
            }
        }
    }
}

fun LazyListScope.chatListContent(
    resource: Resource<List<ChatUiModel>>,
    onClick:() -> Unit
) {
    val chats = resource.data.orEmpty()

    when{
        resource.isLoading ->{
            item {
                LoadingScreen()
            }
        }

        resource.errorMessage !=null && !resource.isLoading-> {
            item {
                ErrorScreen()
            }
        }

        chats.isEmpty() -> {
            item {
                HorizontalTitleItemCountSection(
                    itemCount = chats.size,
                    headerText = "Messages",
                ) { }
                EmptyChatContent()
            }
        }

        else -> {
            item {
                HorizontalTitleItemCountSection(
                    itemCount = chats.size,
                    headerText = "Messages",
                ) { }
            }
            items(
                items = chats,
                key = { it.id }
            ) { chat ->
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    ChatCard(chat = chat, onClick = onClick)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

fun LazyListScope.notificationsListContent(
    resource: Resource<List<NotificationUiModel>>,
    groupNotifications: Map<String, List<NotificationUiModel>>
) {
    when{
        resource.isLoading ->{
            item {
                LoadingScreen()
            }
        }

        resource.errorMessage !=null && !resource.isLoading-> {
            item {
                ErrorScreen()
            }
        }

        groupNotifications.isEmpty() -> {
            item {
                EmptyNotificationContent()
            }
        }

        else -> {
            val groupKeys = groupNotifications.keys.toList()
            groupKeys.forEachIndexed { index, time ->
                val notification = groupNotifications[time] ?: emptyList()
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        HeaderListBlackTitle(title = time)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                items(
                    items = notification,
                    key = { it.id }
                ) { notification ->
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        NotificationCard(notification = notification)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (index < groupKeys.size - 1) {
                    item {
                        Divider32()
                    }
                }
            }
        }
    }
}

@Composable
fun ChatTabContent(
    selectedType: ChatTab,
    onTabSelected: (ChatTab) -> Unit
) {
    val tabs = ChatTab.entries.toTypedArray()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Neutral20,
                shape = RoundedCornerShape(50)
            )
            .padding(6.dp)
    ) {
        Row {
            tabs.forEachIndexed { index, type ->
                val isSelected = type == selectedType
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) Orange500 else Color.Transparent
                        )
                        .clickable { onTabSelected(type)}
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type.title,
                        color = if (isSelected) Neutral10 else Neutral70,
                        style = LocalCustomTypography.current.h7Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatContent(
        uiState = ChatUiState(
            chats = Resource(data = dummyChats),
            notifications = Resource(data = dummyNotifications),
            selectedTab = ChatTab.CHAT
        )
    )
}