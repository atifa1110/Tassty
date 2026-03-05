package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.SenderType
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink600
import com.example.tassty.R
import com.example.tassty.dummyChats
import com.example.tassty.ui.theme.Blue600
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Neutral10

@Composable
fun ChatCard(
    chat: ChatUiModel,
    onClick: () -> Unit
) {
    val badgeColor = when(chat.senderType) {
        SenderType.DRIVER -> Blue600
        SenderType.RESTAURANT -> Orange600
    }

    val badgeIcon = when(chat.senderType) {
        SenderType.DRIVER -> painterResource(R.drawable.ic_delivery)
        SenderType.RESTAURANT -> painterResource(R.drawable.store)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(44.dp)) {
                AsyncImage(
                    model = chat.senderProfileImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )

                if (chat.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Neutral10)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(Green500)
                            .align(Alignment.TopEnd)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(badgeColor)
                        .align(Alignment.BottomStart),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = badgeIcon,
                        contentDescription = null,
                        tint = Neutral10,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.senderName,
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100
                    )
                    Text(
                        text = chat.displayTime,
                        style = LocalCustomTypography.current.bodyTinyMedium,
                        color = Neutral70
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = chat.lastMessage,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = Neutral70,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (chat.unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Orange500),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chat.unreadCount.toString(),
                                color = Neutral10,
                                style = LocalCustomTypography.current.h8Regular
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    ChatCard(chat = dummyChats[0], onClick = {})
}