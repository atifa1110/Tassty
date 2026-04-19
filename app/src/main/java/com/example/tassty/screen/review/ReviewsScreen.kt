package com.example.tassty.screen.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.RatingSummary
import com.example.core.domain.model.StarDistribution
import com.example.core.ui.model.RestaurantReviewUiModel
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HeaderTitleScreen
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.component.ReviewLargeCard
import com.example.tassty.component.ShimmerRatingSummaryCard
import com.example.tassty.component.ShimmerReviewLargeCard
import com.example.tassty.component.StarRow
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.RestaurantPreviewData

@Composable
fun ReviewScreen(
    onNavigateBack:() -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )


}

@Composable
fun ReviewContent(
    uiState: ReviewUiState,
    onNavigateBack: () -> Unit
) {
    val resource = uiState.resource
    val isLoading = resource.isLoading
    val data = resource.data
    val error = resource.errorMessage

    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = { BackTopAppBar(onBackClick = onNavigateBack) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item (key= "title_header"){
                HeaderTitleScreen(
                    modifier = Modifier.padding(24.dp),
                    title = "Review & Ratings."
                )
            }

            if (error != null && !isLoading) {
                item(key= "error_screen") {
                    ErrorScreen()
                }
            }

            else if (isLoading) {
                item (key = "load_rating_summary"){
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        ShimmerRatingSummaryCard()
                    }
                    Divider32()
                }
                items(5) {
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        ShimmerReviewLargeCard()
                    }
                }

            } else {
                data?.let {
                    item(key = "rating_summary") {
                        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                            RatingSummaryCard(
                                averageRating = it.summary.averageRating,
                                totalReviews = it.summary.totalReviews,
                                distribution = it.summary.distribution
                            )
                        }
                        Divider32()
                    }
                    items(items = it.reviews, key = { it.id }) { item ->
                        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                            ReviewLargeCard(review = item)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RatingSummaryCard(
    averageRating: Double = 4.9,
    totalReviews: Int = 200,
    distribution: List<StarDistribution> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, LocalCustomColors.current.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingScoreSection(
                modifier = Modifier.weight(0.4f),
                averageRating = averageRating,
                totalReviews = totalReviews
            )

            RatingDistributionSection(
                modifier = Modifier.weight(0.6f),
                distribution = distribution
            )
        }
    }
}

@Composable
private fun RatingScoreSection(
    modifier: Modifier = Modifier,
    averageRating: Double,
    totalReviews: Int
) {
    Column(
        modifier = modifier
            .background(LocalCustomColors.current.cardBackground)
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = averageRating.toString(),
            style = LocalCustomTypography.current.h2Bold,
            color = LocalCustomColors.current.headerText
        )
        Spacer(Modifier.height(2.dp))
        StarRow(averageRating = averageRating.toInt())
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.reviews, totalReviews),
            style = LocalCustomTypography.current.bodySmallMedium,
            color = LocalCustomColors.current.text
        )
    }
}

@Composable
private fun RatingDistributionSection(
    modifier: Modifier = Modifier,
    distribution: List<StarDistribution>
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (5 downTo 1).forEach { starLevel ->
            val item = distribution.find { it.star == starLevel }
            RatingBarRow(
                starLevel = starLevel,
                percentage = item?.percentage ?: 0
            )
        }
    }
}

@Composable
private fun RatingBarRow(
    starLevel: Int,
    percentage: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$starLevel",
            style = LocalCustomTypography.current.bodyXtraSmallMedium,
            color = LocalCustomColors.current.headerText,
            modifier = Modifier.width(12.dp)
        )
        Icon(
            painter = painterResource(R.drawable.star),
            contentDescription = null,
            tint = Orange500,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(8.dp))

        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = Orange500,
            trackColor = Neutral30
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "$percentage%",
            style = LocalCustomTypography.current.bodyXtraSmallMedium,
            color = LocalCustomColors.current.text,
            modifier = Modifier.width(35.dp),
            textAlign = TextAlign.End
        )
    }
}


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun ReviewLightPreview(){
//    TasstyTheme {
//        ReviewContent(
//            uiState = ReviewUiState(
//                resource = Resource(
//                    data = RestaurantReviewUiModel(
//                        summary = RatingSummary(
//                            averageRating = 4.0, totalReviews = 200, distribution =
//                                listOf(
//                                    StarDistribution(5, 100, 70),
//                                    StarDistribution(4, 60, 50),
//                                    StarDistribution(3, 80, 40),
//                                    StarDistribution(2, 10, 20),
//                                    StarDistribution(1, 100, 70)
//                                )
//                        ), reviews = reviews
//                    )
//                )
//            ),
//            onNavigateBack = {}
//        )
//    }
//}


//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun ReviewDarkPreview(){
    TasstyTheme (darkTheme = true){
        ReviewContent(
            uiState = ReviewUiState(
                resource = Resource(
                    data = RestaurantReviewUiModel(
                        summary = RatingSummary(
                            averageRating = 4.0, totalReviews = 200, distribution =
                                listOf(
                                    StarDistribution(5, 100, 70),
                                    StarDistribution(4, 60, 50),
                                    StarDistribution(3, 80, 40),
                                    StarDistribution(2, 10, 20),
                                    StarDistribution(1, 100, 70)
                                )
                        ), reviews = RestaurantPreviewData.reviews
                    )
                )
            ),
            onNavigateBack = {}
        )
    }
}