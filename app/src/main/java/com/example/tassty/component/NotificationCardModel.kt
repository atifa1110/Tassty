package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.NotificationType
import com.example.core.ui.model.NotificationUiModel
import com.example.tassty.ui.theme.Green100
import com.example.tassty.ui.theme.Green600
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
import com.example.tassty.dummyNotifications

@Composable
fun NotificationCard(
    notification: NotificationUiModel,
    modifier: Modifier = Modifier
) {
    val (icon, backgroundColor, iconColor) = when (notification.type) {
        NotificationType.ORDER -> Triple(painterResource(R.drawable.ic_delivery), Green100, Green600)
        NotificationType.DISCOUNT -> Triple(painterResource(R.drawable.promo), Orange100, Orange600)
        NotificationType.PROMO -> Triple(painterResource(R.drawable.ic_delivery), Pink100, Pink600)
        NotificationType.TRANSACTION -> Triple(painterResource(R.drawable.credit_card), Pink100, Pink600)
    }

    Card (
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral20)
    ) {
        Row (
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2. Teks Konten
            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        style = LocalCustomTypography.current.h6Bold,
                        color = Neutral100,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = notification.displayTime,
                        style = LocalCustomTypography.current.bodyTinyMedium,
                        color = Neutral70
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = notification.description,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = Neutral70,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Bottom)
                                .padding(start = 8.dp)
                                .size(10.dp)
                                .background(Orange500, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationCardPreview(){
    NotificationCard(
        notification = dummyNotifications[0]
    )
}