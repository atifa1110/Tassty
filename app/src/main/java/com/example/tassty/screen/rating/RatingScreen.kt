package com.example.tassty.screen.rating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tassty.R
import com.example.tassty.RatingType
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CommonImage
import com.example.tassty.component.CustomRatingChip
import com.example.tassty.component.CustomTwoColorText
import com.example.tassty.component.Divider32
import com.example.tassty.component.FeedbackSection
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.OrderDetailAppBar
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun RatingScreen(
    onNavigateBack:() -> Unit,
    viewModel: RatingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event ->
            when(event){
                is RatingEvent.NavigateBack -> TODO()
                is RatingEvent.ShowMessage -> TODO()
            }
        }
    }
    RatingContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRatingChanged = viewModel::onRatingChanged,
        onSelectedChanged = viewModel::onSelectedChanged,
        onFeedbackChanged = viewModel::onFeedbackChanged,
        onSubmitRating = viewModel::onSubmitRating
    )
}

@Composable
fun RatingContent(
    uiState: RatingUiState,
    onNavigateBack:() -> Unit,
    onRatingChanged: (Int) -> Unit,
    onSelectedChanged:(String) -> Unit,
    onFeedbackChanged:(String) -> Unit,
    onSubmitRating:() -> Unit
) {
    Scaffold (
        containerColor = LocalCustomColors.current.background,
        topBar = {
            BackTopAppBar(onBackClick = onNavigateBack)
        },
        bottomBar = {
            Column(Modifier
                .fillMaxWidth().background(LocalCustomColors.current.modalBackgroundFrame)
                .padding(
                    start = 24.dp, end = 24.dp,
                    top = 24.dp, bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ButtonComponent(
                    modifier = Modifier.fillMaxWidth() ,
                    enabled = uiState.isButtonEnabled,
                    labelResId = R.string.submit,
                    onClick = onSubmitRating
                )

                CustomTwoColorText(
                    modifier = Modifier.fillMaxWidth(),
                    fullText = "By submitting, you agree to our Terms and conditions",
                    highlightText = "Terms and conditions",
                )
            }
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            HeaderRating(
                title = uiState.headerName,
                orderNumber = uiState.orderNumber,
                createdAt = uiState.createdAt
            )
            Divider32()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if(uiState.type == RatingType.DRIVER){
                        CommonImage(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape),
                            imageUrl = uiState.imageUrl,
                            name = uiState.name
                        )
                    }else {
                        CommonImage(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            imageUrl = uiState.imageUrl,
                            name = uiState.name
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = uiState.name,
                            style = LocalCustomTypography.current.h2Bold,
                            textAlign = TextAlign.Center,
                            color = LocalCustomColors.current.headerText
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.how_much_rating),
                            style = LocalCustomTypography.current.bodyMediumMedium,
                            color = LocalCustomColors.current.text,
                            textAlign = TextAlign.Center
                        )
                    }

                    BasicRatingBar(
                        rating = uiState.rating,
                        onRatingChanged = onRatingChanged
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    if(uiState.type == RatingType.MENU) {
                        TagsSection(
                            list = uiState.tagList,
                            selectedTags = uiState.selectedTags,
                            onSelectChanged = onSelectedChanged
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    FeedbackSection(
                        feedback = uiState.feedback,
                        onFeedbackChanged = onFeedbackChanged
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderRating(
    title: String,
    orderNumber: String,
    createdAt: String
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            text = title,
            style = LocalCustomTypography.current.h2Bold,
            color = LocalCustomColors.current.headerText
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeaderIconText(
                text = orderNumber
            )
            Text(
                text = "•",
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = Neutral40
            )
            HeaderIconText(
                icon = R.drawable.clock,
                iconColor = Blue500,
                text = createdAt
            )
        }
    }
}

@Composable
fun HeaderIconText(
    modifier: Modifier = Modifier,
    iconColor: Color = Orange500,
    icon: Int = R.drawable.shopping_bag,
    style: TextStyle = LocalCustomTypography.current.bodyMediumMedium,
    text: String
){
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
                .align(Alignment.CenterVertically),
            tint = iconColor,
        )
        Text(
            text = text,
            style = style,
            color = LocalCustomColors.current.text
        )
    }
}

@Composable
fun TagsSection(
    list: List<String>,
    selectedTags: Set<String>,
    onSelectChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderListBlackTitle(title = "What did you love about the food?")

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            list.forEach { tag ->
                CustomRatingChip(
                    text = tag,
                    isSelected = selectedTags.contains(tag),
                    onClick = {onSelectChanged(tag)}
                )
            }
        }
    }
}

@Composable
fun BasicRatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 1..maxRating) {
            val isSelected = i <= rating
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick =  { onRatingChanged(i) }),
                tint = if (isSelected) Orange500 else Neutral30
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun RatingLightPreview() {
//    TasstyTheme {
//        RatingContent(
//            uiState = RatingUiState(
//                headerName = "Indah Cafe",
//                name = "Fresh Salad",
//                type = RatingType.DRIVER,
//                rating = 4,
//                selectedTags = setOf("Taste", "Portion", "Freshness"),
//                orderNumber = "T-543534534534",
//                orderItemId = "",
//                createdAt = "Today, 09:35 AM",
//                isButtonEnabled = false
//            ),
//            onNavigateBack = {},
//            onRatingChanged = {},
//            onSelectedChanged = {},
//            onFeedbackChanged = {},
//            onSubmitRating = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun RatingDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        RatingContent(
//            uiState = RatingUiState(
//                headerName = "Indah Cafe",
//                name = "Fresh Salad",
//                type = RatingType.DRIVER,
//                rating = 4,
//                selectedTags = setOf("Taste", "Portion", "Freshness"),
//                orderNumber = "T-543534534534",
//                orderItemId = "",
//                createdAt = "Today, 09:35 AM",
//                isButtonEnabled = false
//            ),
//            onNavigateBack = {},
//            onRatingChanged = {},
//            onSelectedChanged = {},
//            onFeedbackChanged = {},
//            onSubmitRating = {}
//        )
//    }
//}