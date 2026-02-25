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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.ReviewUiModel
import com.example.tassty.R
import com.example.tassty.reviews
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Pink500

@Composable
fun ReviewCard(
    review: ReviewUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(250.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral10),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                CommonImage(
                    imageUrl = review.profileImage,
                    name= "Profile picture of ${review.username}",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Column(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        repeat(review.rating) {
                            Icon(
                                painter = painterResource(R.drawable.star),
                                contentDescription = null,
                                tint = Orange500,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }


                    Row (horizontalArrangement = Arrangement.spacedBy(4.dp)){
                        Text(
                            text = review.username,
                            style = LocalCustomTypography.current.h7Bold,
                            color = Neutral100
                        )
                        Text(
                            text = " • ",
                            style = LocalCustomTypography.current.bodyTinyMedium,
                            color = Neutral70
                        )

                        Text(
                            text = review.date,
                            style = LocalCustomTypography.current.bodyTinyMedium,
                            color = Neutral70
                        )
                    }

                    Text(
                        text = review.comment,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = Neutral70,
                        maxLines = 2,
                        minLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

        }
    }
}


@Composable
fun ReviewLargeCard(review: ReviewUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            CommonImage(
                imageUrl = "",
                name = "profile reviewers",
                modifier = Modifier.size(44.dp).clip(CircleShape)
            )

            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    repeat(review.rating) {
                        Icon(
                            painter = painterResource(R.drawable.star),
                            contentDescription = "review stars",
                            tint = Orange500, // Warna emas/oranye untuk bintang
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Row (horizontalArrangement = Arrangement.spacedBy(4.dp)){
                    Text(
                        text = review.username,
                        style = LocalCustomTypography.current.h5Bold, // Asumsi bodyMediumBold
                        color = Neutral100
                    )
                    Text(
                        text = " • ",
                        style = LocalCustomTypography.current.bodySmallMedium, // Asumsi bodyMediumMedium
                        color = Neutral70
                    )

                    Text(
                        text = review.date,
                        style = LocalCustomTypography.current.bodySmallMedium, // Asumsi bodyMediumMedium
                        color = Neutral70
                    )
                }
            }
        }

        val triangleOffset = 16.dp

        Box(modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawBubbleTail(
                            xOffset = triangleOffset.toPx(),
                            yOffset = 0f,
                            color = Neutral20
                        )
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(Neutral20)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Indah Café offers a fantastic dining experience with a warm ambiance and friendly staff. It's the perfect place for a special occasion or simply to enjoy a great meal with loved ones. I look forward to my next visit!",
                    style = LocalCustomTypography.current.bodySmallRegular,
                    color = Neutral70
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)){
            Icon(
                painter = painterResource(R.drawable.clipboard_list),
                tint = Pink500,
                contentDescription = "order review"
            )
            Text(
                text = "Order: ",
                style = LocalCustomTypography.current.bodyXtraSmallSemiBold,
                color = Neutral100
            )
            Text(
                text = review.orderItems,
                style = LocalCustomTypography.current.bodyXtraSmallMedium,
                color = Neutral70
            )
        }
    }
}

fun DrawScope.drawBubbleTail(xOffset: Float, yOffset: Float, color: Color) {
    val tailWidth = 10.dp.toPx()
    val tailHeight = 8.dp.toPx()

    // Path untuk bentuk segitiga (menunjuk ke atas)
    val path = Path().apply {
        // Mulai dari sudut kanan bawah segitiga
        moveTo(xOffset + tailWidth / 2f, yOffset)
        // Pindah ke puncak (ujung) segitiga
        lineTo(xOffset, yOffset - tailHeight) // Naik ke puncak
        // Pindah ke sudut kiri bawah segitiga
        lineTo(xOffset - tailWidth / 2f, yOffset)
        close()
    }

    drawPath(path, color = color)
}


@Preview(showBackground = true)
@Composable
fun PreviewReviewCard() {
    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ReviewCard(review = reviews[0])
    }
}
