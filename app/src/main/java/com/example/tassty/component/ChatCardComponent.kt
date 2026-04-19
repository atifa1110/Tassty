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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.SenderType
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.R
import com.example.tassty.ui.theme.Blue600
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Neutral10
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.core.ui.model.MessageUiModel
import com.example.tassty.ui.theme.Green400
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.ChatData


@Composable
fun ChatCard(
    chat: ChatUiModel,
    onClick: () -> Unit
) {
    val badgeColor = when(chat.senderType) {
        SenderType.DRIVER -> Blue600
        SenderType.RESTAURANT -> Orange600
        SenderType.USER -> Green400
    }

    val badgeIcon = when(chat.senderType) {
        SenderType.DRIVER -> R.drawable.ic_delivery
        SenderType.RESTAURANT -> R.drawable.store
        SenderType.USER -> R.drawable.chat
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(modifier = Modifier.size(44.dp)) {
                ChatProfile(
                    imageUrl = chat.profileImage,
                    name = chat.senderName
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

                CircleImageIcon(
                    boxColor = badgeColor,
                    contentDescription = "",
                    icon = badgeIcon,
                    iconSize = 10.dp,
                    iconColor = Neutral10,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomStart),
                )
            }


            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = chat.senderName,
                        style = LocalCustomTypography.current.h6Bold,
                        color = LocalCustomColors.current.headerText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = chat.displayTime,
                        style = LocalCustomTypography.current.bodyTinyMedium,
                        color = LocalCustomColors.current.text
                    )
                }

                Spacer(Modifier.height(2.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = chat.lastMessage,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color =LocalCustomColors.current.text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (chat.unreadCount > 0) {
                        Spacer(Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .size(18.dp)
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

@Composable
fun ShimmerChatCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(44.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .shimmerLoadingAnimation()
                )

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Neutral10)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .shimmerLoadingAnimation()
                        .align(Alignment.BottomStart)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerLoadingAnimation()
                    )
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerLoadingAnimation()
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerLoadingAnimation()
                    )
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .shimmerLoadingAnimation()
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: MessageUiModel,
    onImageClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (message.isMine) {
            Spacer(modifier = Modifier.weight(0.4f))
        }
        if (!message.isMine) {
            MessageProfile(
                imageUrl = message.senderImage,
                name = message.senderName
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(
            modifier = Modifier.weight(1f, fill = false).padding(top = 10.dp),
            horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start
        ) {

            if (message.text.isNotEmpty()) {
                Surface(
                    shape = ChatBubbleShape(message.isMine),
                    color = if (message.isMine) Orange500 else LocalCustomColors.current.cardBackground
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier.padding(12.dp),
                        color = if (message.isMine) Neutral10 else LocalCustomColors.current.text,
                        style = LocalCustomTypography.current.bodySmallRegular
                    )
                }
            }

            if(message.imageAttachment.isNotEmpty()){
                CommonImage(
                    imageUrl = message.imageAttachment,
                    name = "attachment",
                    modifier = Modifier.size(72.dp).
                    clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onImageClick)
                )
            }

            ChatStatus(isSeen = message.isSeen,time = message.time,isMine = message.isMine)
        }

        if (message.isMine) {
            Spacer(modifier = Modifier.width(16.dp))
            MessageProfile(
                imageUrl = message.senderImage,
                name = message.senderName
            )
        }

        if (!message.isMine) {
            Spacer(modifier = Modifier.weight(0.4f))
        }
    }
}

@Composable
fun ChatStatus(isSeen: Boolean, time: String, isMine: Boolean){
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = LocalCustomTypography.current.bodyXtraSmallMedium.toSpanStyle()
                    .copy(
                        color = LocalCustomColors.current.text
                    )
            ) {
                append(time)
            }

            if (isSeen && isMine) {
                withStyle(style = SpanStyle(color = LocalCustomColors.current.text.copy(0.4f))) {
                    append("  •  ")
                }

                withStyle(
                    style = LocalCustomTypography.current.bodyXtraSmallSemiBold.toSpanStyle()
                        .copy(
                            color = Orange500
                        )
                ) {
                    append("Seen")
                }
            }
        },
        modifier = Modifier.padding(top = 8.dp),
        textAlign = TextAlign.Center
    )
}

//@Preview(showBackground = true)
@Composable
fun ChatListPreview() {
    Column(Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ChatCard(
            chat = ChatData.dummyChats[0],
            onClick = {}
        )
        ShimmerChatCard()
        ChatBubble(
            message = ChatData.dummyMessages[0],
            onImageClick = {}
        )
    }
}
