package com.example.tassty.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.component.AllReviewChip
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.Personal30Chip
import com.example.tassty.component.Personal50Chip
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.component.ReviewLargeCard
import com.example.tassty.reviews
import com.example.tassty.ui.theme.Neutral10

@Composable
fun ReviewScreen(){
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            BackTopAppBar(onBackClick = {})
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().background(Neutral10)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(top=32.dp, start = 24.dp,end=24.dp)
            ) {
                Text(
                    text = "Review & Ratings.",
                    style = LocalCustomTypography.current.h2Bold,
                    color = Neutral100
                )
                Spacer(Modifier.height(24.dp))
                RatingSummaryCard(Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()) // 👉 bikin Row bisa discroll horizontal
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                AllReviewChip()
                Personal50Chip()
                Personal30Chip()
            }

            HorizontalDivider(Modifier.padding(vertical = 32.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                items(items = reviews , key ={it.id}) { item ->
                    ReviewLargeCard(
                        review = item
                    )
                }
            }
        }
    }
}


@Composable
fun RatingSummaryCard(
    modifier: Modifier = Modifier,
    averageRating: Double = 4.9,
    totalReviews: Int = 200,
    distribution: Map<Int, Int> = mapOf(
        1 to 83,
        2 to 9,
        3 to 4,
        4 to 4,
        5 to 0,
    )
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, Neutral30)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left side: rating + stars + review count
            Column(
                modifier = Modifier.background(Neutral20).weight(0.4f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = String.format("%.1f", averageRating),
                        style = LocalCustomTypography.current.h2Bold,
                        color = Neutral100
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { i ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (i < averageRating.toInt()) Color(0xFFFF9800) else Color.LightGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "$totalReviews+ reviews",
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = Neutral70
                )
            }

            // Right side: distribution
            Column(
                modifier = Modifier.weight(0.6f).padding(top=16.dp, end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val total = distribution.values.sum().coerceAtLeast(1)
                (5 downTo 1).forEach { star ->
                    val percent = distribution[star]?.toFloat()?.div(total) ?: 0f
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$star",
                            style = LocalCustomTypography.current.bodyXtraSmallMedium,
                            color = Neutral100
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(8.dp))

                        LinearProgressIndicator(
                            progress = { percent },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50)),
                            color = Color(0xFFFF9800),
                            trackColor = Color(0xFFE0E0E0),
                        )

                        Spacer(Modifier.width(4.dp))

                        Text(
                            "${(percent * 100).toInt()}%",
                            style = LocalCustomTypography.current.bodyXtraSmallMedium,
                            color = Neutral70
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReviewPreviewScreen(){
    ReviewScreen()
}