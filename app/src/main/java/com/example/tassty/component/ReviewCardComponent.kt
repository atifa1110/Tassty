package com.example.tassty.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.ReviewUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.reviews
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Pink500

@Composable
fun ReviewCard(
    review: ReviewUiModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(250.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.background),
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
                    StarRow(
                        size = 12.dp,
                        averageRating = review.rating
                    )
                    Row (horizontalArrangement = Arrangement.spacedBy(4.dp)){
                        Text(
                            text = review.username,
                            style = LocalCustomTypography.current.h7Bold,
                            color = LocalCustomColors.current.headerText
                        )
                        Text(
                            text = " • ",
                            style = LocalCustomTypography.current.bodyTinyMedium,
                            color = LocalCustomColors.current.text
                        )

                        Text(
                            text = review.date,
                            style = LocalCustomTypography.current.bodyTinyMedium,
                            color = LocalCustomColors.current.text
                        )
                    }

                    Text(
                        text = review.comment,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = LocalCustomColors.current.text,
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
fun ReviewLargeCard(
    review: ReviewUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColors.current.background)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CommonImage(
                imageUrl = "",
                name = "profile reviewers",
                modifier = Modifier.size(44.dp).clip(CircleShape)
            )

            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StarRow(averageRating = review.rating)
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = review.username,
                        style = LocalCustomTypography.current.h5Bold,
                        color = LocalCustomColors.current.headerText
                    )
                    Text(
                        text = " • ",
                        style = LocalCustomTypography.current.bodySmallRegular,
                        color = LocalCustomColors.current.text
                    )

                    Text(
                        text = review.date,
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = LocalCustomColors.current.text
                    )
                }
            }
        }

        val triangleOffset = 16.dp
        val bubbleColor = LocalCustomColors.current.cardBackground

        Box(modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawBubbleTail(
                            xOffset = triangleOffset.toPx(),
                            yOffset = 0f,
                            color = bubbleColor
                        )
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalCustomColors.current.cardBackground)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Indah Café offers a fantastic dining experience with a warm ambiance and friendly staff. It's the perfect place for a special occasion or simply to enjoy a great meal with loved ones. I look forward to my next visit!",
                    style = LocalCustomTypography.current.bodySmallRegular,
                    color = LocalCustomColors.current.text
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
                color = LocalCustomColors.current.headerText
            )
            Text(
                text = review.orderItems,
                style = LocalCustomTypography.current.bodyXtraSmallMedium,
                color = LocalCustomColors.current.text
            )
        }
    }
}

fun DrawScope.drawBubbleTail(xOffset: Float, yOffset: Float, color: Color) {
    val tailWidth = 10.dp.toPx()
    val tailHeight = 8.dp.toPx()

    val path = Path().apply {
        moveTo(xOffset + tailWidth / 2f, yOffset)
        lineTo(xOffset, yOffset - tailHeight)
        lineTo(xOffset - tailWidth / 2f, yOffset)
        close()
    }

    drawPath(path, color = color)
}

@Composable
fun StarRow(
    size: Dp = 16.dp,
    averageRating: Int
){
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { i ->
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = null,
                tint = if (i < averageRating) Orange500 else Neutral40,
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
fun ShimmerReviewLargeCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalCustomColors.current.background)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .shimmerLoadingAnimation()
            )

            Column(
                modifier = Modifier.padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .size(80.dp, 12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoadingAnimation()
                )
                Spacer(
                    modifier = Modifier
                        .size(150.dp, 16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerLoadingAnimation()
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerLoadingAnimation()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .shimmerLoadingAnimation()
            )
            Spacer(
                modifier = Modifier
                    .size(200.dp, 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerLoadingAnimation()
            )
        }
    }
}

@Composable
fun ShimmerRatingSummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth().height(136.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Neutral30)
    ) {
        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(0.4f).background(Neutral20)
                    .fillMaxHeight().padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.size(60.dp, 30.dp).shimmerLoadingAnimation())
                Spacer(Modifier.height(8.dp))
                Spacer(Modifier.size(80.dp, 14.dp).shimmerLoadingAnimation())
                Spacer(Modifier.height(12.dp))
                Spacer(Modifier.size(100.dp, 12.dp).shimmerLoadingAnimation())
            }
            Column(
                modifier = Modifier.weight(0.6f).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(5) {
                    Spacer(Modifier.fillMaxWidth().height(8.dp)
                        .shimmerLoadingAnimation())
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewReviewCard() {
//    Column(modifier = Modifier.padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        ReviewLargeCard(review = reviews[0])
//        ShimmerReviewLargeCard()
//    }
//}
