package com.example.tassty.screen.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.NotificationUiModel
import com.example.tassty.ChatTab
import com.example.tassty.component.ChatCard
import com.example.tassty.component.ChatRemoveContent
import com.example.tassty.component.ChatTopAppBar
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.DeleteNotificationContent
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyChatContent
import com.example.tassty.component.EmptyNotificationContent
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.HorizontalTitleItemCountSection
import com.example.tassty.component.HorizontalTitleSection
import com.example.tassty.component.NotificationCard
import com.example.tassty.component.ShimmerChatCard
import com.example.tassty.component.ShimmerNotificationCard
import com.example.tassty.component.SwipeableItemWithActions
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.ChatData
import com.example.tassty.util.UserData

@Composable
fun ChatScreen(
    onNavigateToMessage:(String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event->
            when(event){
                is ChatEvent.ShowMessage -> {
                    Toast.makeText(context,event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ChatContent(
        uiState = uiState,
        onTabSelected = viewModel::onSelectedTab,
        onNavigateToMessage = onNavigateToMessage,
        onRevealChange = viewModel::onRevealChange,
        onSelectedDeleteChat = viewModel::onSelectedChat,
        onNotificationClick = viewModel::onNotificationClick,
        onDeleteNotification = viewModel::onDeleteClick
    )

    CustomBottomSheet(
        visible = uiState.isDeleteChatSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = { }
    ) {
        ChatRemoveContent(
            chat = uiState.selectedChat,
            onDismiss = viewModel::onDismissDeleteChat,
            onRemoveChat = viewModel::onRemoveChat
        )
    }


    CustomBottomSheet(
        visible = uiState.isDeleteNotifSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        DeleteNotificationContent(
            onDismissClick = viewModel::onDismissDelete,
            onDelete = viewModel::onDeleteAllNotification
        )
    }
}

@Composable
fun ChatContent(
    uiState: ChatUiState,
    onTabSelected: (ChatTab) -> Unit,
    onNavigateToMessage:(String) -> Unit,
    onRevealChange: (String, Boolean) -> Unit,
    onSelectedDeleteChat:(ChatUiModel) -> Unit,
    onNotificationClick:(String) -> Unit,
    onDeleteNotification:() -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            ChatTopAppBar(onDeleteClick = onDeleteNotification)
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item(key="header") {
                ChatTabContent(
                    selectedType = uiState.selectedTab,
                    onTabSelected = onTabSelected
                )
            }

            when (uiState.selectedTab) {
                ChatTab.CHAT -> {
                    chatListContent(
                        resource = uiState.chats,
                        onClick = onNavigateToMessage,
                        onRevealChange = onRevealChange,
                        onSelectedDeleteChat = onSelectedDeleteChat
                    )
                }
                ChatTab.NOTIFICATION -> {
                    notificationsListContent(
                        resource = uiState.notifications,
                        groupNotifications = uiState.groupedNotifications,
                        onNotificationClick = onNotificationClick
                    )
                }
            }
        }
    }
}

fun LazyListScope.chatListContent(
    resource: Resource<List<ChatUiModel>>,
    onClick:(String) -> Unit,
    onRevealChange: (String, Boolean) -> Unit,
    onSelectedDeleteChat:(ChatUiModel)-> Unit
) {
    val chats = resource.data.orEmpty()

    when{
        resource.isLoading ->{
            items(4) {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    ShimmerChatCard()
                    Spacer(Modifier.height(12.dp))
                }
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
            itemsIndexed(
                items = chats,
                key = { _, chat -> chat.id }
            ) { index, chat ->
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    SwipeableItemWithActions(
                        isRevealed = chat.isSwipeActionVisible,
                        onExpanded = {
                            onRevealChange(chat.id, true)
                        },
                        onCollapsed = {
                            onRevealChange(chat.id, false)
                        },
                        actions = {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(72.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 20.dp,
                                            bottomStart = 20.dp
                                        )
                                    )
                                    .background(Pink500),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(onClick = {onSelectedDeleteChat(chat)}) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    ) {
                        ChatCard(chat = chat, onClick = { onClick(chat.id) })
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

fun LazyListScope.notificationsListContent(
    resource: Resource<List<NotificationUiModel>>,
    groupNotifications: Map<String, List<NotificationUiModel>>,
    onNotificationClick:(String) -> Unit
) {
    when{
        resource.isLoading ->{
            items(4) {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    ShimmerNotificationCard()
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        resource.errorMessage !=null && !resource.isLoading-> {
            item {
                ErrorScreen()
            }
        }

        groupNotifications.isEmpty() -> {
            item {
                HorizontalTitleSection(title = "Today") { }
                Box(
                    modifier = Modifier
                        .fillParentMaxHeight(0.7f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyNotificationContent()
                }
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
                    Column (modifier = Modifier.padding(horizontal = 24.dp)) {
                        NotificationCard(
                            notification = notification,
                            onClick = { if(!notification.isRead) onNotificationClick(notification.id) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
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
                color = LocalCustomColors.current.cardBackground,
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
                        color = if (isSelected) Neutral10 else LocalCustomColors.current.text,
                        style = LocalCustomTypography.current.h7Bold
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ChatLightPreview() {
    TasstyTheme {
        ChatContent(
            uiState = ChatUiState(
                chats = Resource(data = ChatData.dummyChats),
                notifications = Resource(data = ChatData.dummyNotifications),
                selectedTab = ChatTab.CHAT
            ),
            onNavigateToMessage = {},
            onTabSelected = {},
            onRevealChange = { _, _ -> },
            onSelectedDeleteChat = {},
            onNotificationClick = {},
            onDeleteNotification = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun ChatDarkPreview() {
    TasstyTheme(darkTheme = true) {
        ChatContent(
            uiState = ChatUiState(
                chats = Resource(data = ChatData.dummyChats),
                notifications = Resource(data = ChatData.dummyNotifications),
                selectedTab = ChatTab.CHAT
            ),
            onNavigateToMessage = {},
            onTabSelected = {},
            onRevealChange = { _, _ -> },
            onSelectedDeleteChat = {},
            onNotificationClick = {},
            onDeleteNotification = {}
        )
    }
}