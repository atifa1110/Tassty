package com.example.tassty.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.core.ui.model.RestaurantStatus
import com.example.tassty.R
import com.example.tassty.categoriesItem
import com.example.tassty.model.Category
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun HeaderWithOverlap(
    imageUrl: String,
    status : RestaurantStatus,
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit,
    collapseProgress: Float,
    headerContent: @Composable () -> Unit
) {
    val imageHeight = 304.dp
    val searchBarOverlapHeight = 24.dp

    val currentHeight = lerp(imageHeight,100.dp, collapseProgress)
    val contentAlpha = 1f - collapseProgress

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(currentHeight)
    ) {
        ItemImage(
            imageUrl = imageUrl,
            name = "category header image",
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight).alpha(contentAlpha)
                .align(Alignment.TopCenter)
        )

        CategoryTopAppBar(onBackClick = onBackClick, onFilterClick = onFilterClick)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter).alpha(contentAlpha),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Neutral10.copy(0.7f)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    .padding(bottom = 48.dp)
            ) {
                headerContent()
            }
        }

        SearchBarWhiteSection(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter).alpha(contentAlpha)
                .offset(y = searchBarOverlapHeight)
                .padding(horizontal = 24.dp)
        )
    }
}

@Composable
fun CategoryFoundHeader(
    searchQuery: String,
    filteredCategories: List<Category>
) {
    val headerText = if (searchQuery.isBlank()) {
        "Categories"
    } else {
        if (filteredCategories.isEmpty()) "Category found"
        else "Category found"
    }

    val styledText: AnnotatedString = buildAnnotatedString {
        withStyle(
            style = LocalCustomTypography.current.h4Bold.toSpanStyle()
                .copy(color = Orange500)
        ) {
            append("${filteredCategories.size} ")
        }
        withStyle(
            style = LocalCustomTypography.current.h5Bold.toSpanStyle()
                .copy(color = Neutral100)
        ) {
            append(headerText)
        }
    }

    Text(text = styledText)
}

@Composable
fun CategoryTextHeader(
    title : String,
    subtitle: String
){
    Column{
        Text(
            text = title,
            style = LocalCustomTypography.current.h3Bold,
            color = Neutral100
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Neutral70
        )
    }
}

@Composable
fun CategoryAndDescriptionHeader(
    category: String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryTextHeader(title = category, subtitle = "Some lunch boosters!")
        CategoryCard(category = categoriesItem)
    }
}

@Composable
fun BestSellerHeader(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryTextHeader(title = "Our best seller!",
            subtitle = "Top-selling delicacies you can't miss!")
        BestSellerIcon()
    }
}

@Composable
fun StatusTextHeader(
    title : String,
    subtitle : String
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = title,
            style = LocalCustomTypography.current.h2Bold,
            color = Neutral100,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = subtitle,
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = Neutral70,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    content : @Composable ()-> Unit
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun HeaderListTitleButton(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color,
    textButton: String = stringResource(R.string.see_all),
    onClick:() -> Unit,
){
    Header(modifier = modifier) {
        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = titleColor
        )
        Text(
            modifier = Modifier.clickable(onClick = onClick),
            text = textButton,
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = Orange500
        )
    }
}

@Composable
fun HeaderListTitleSubtitleButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle : String,
    textButton: String = stringResource(R.string.see_all),
    onClick:() -> Unit,
){
    Header(modifier = modifier) {
        Column {
            Text(
                text = title,
                color = Neutral100,
                style = LocalCustomTypography.current.h5Bold
            )
            Text(
                text = subtitle,
                color = Neutral100,
                style = LocalCustomTypography.current.bodySmallRegular
            )
        }
        Text(
            modifier = Modifier.clickable(onClick = onClick),
            text = textButton,
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = Orange500
        )
    }
}

@Composable
fun HeaderListBlackTitle(
    modifier: Modifier = Modifier,
    title: String
){
    Header(modifier = modifier) {
        Text(
            text = title,
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100
        )
    }
}

@Composable
fun ItemCountTitleText(
    itemCount : Int,
    title : String
){
    Text(
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h4Bold.toSpanStyle().copy(color = Orange500)) {
                append(itemCount.toString())
            }
            append(" ")
            withStyle(style = LocalCustomTypography.current.h5Bold.toSpanStyle().copy(color = Neutral100)) {
                append(title)
            }
        }
    )
}

@Composable
fun HeaderListItemCountTitle(
    modifier: Modifier = Modifier,
    itemCount : Int,
    title : String
){
    Header(modifier = modifier) {
        ItemCountTitleText(itemCount = itemCount, title = title)
    }
}

@Composable
fun HeaderListItemCountTitleButton(
    modifier: Modifier = Modifier,
    itemCount : Int,
    title : String,
    textButton: String = stringResource(R.string.see_all),
    onClick:() -> Unit
){
    Header(modifier = modifier) {
        ItemCountTitleText(itemCount = itemCount, title = title)
        Text(
            modifier = Modifier.clickable(onClick = onClick),
            text = textButton,
            style = LocalCustomTypography.current.bodyMediumMedium,
            color = Orange500
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHeader(){
//    Column (Modifier.fillMaxWidth().padding(24.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)){
//
//        HeaderListTitleButton(
//            title = "Recommended Restaurants",
//            titleColor = Neutral100,
//            onClick = {}
//        )
//
//        HeaderListTitleSubtitleButton(
//            title = "Recommended Restaurants",
//            subtitle = "Our recommended cafes to explore!",
//            onClick = {}
//        )
//
//        HeaderListBlackTitle(
//            title = "Suggested menus for you!"
//        )
//
//        HeaderListItemCountTitle(
//            itemCount = 24,
//            title = "Search founds"
//        )
//
//        HeaderListItemCountTitleButton(
//            itemCount = 24,
//            title = "Search founds",
//            onClick = {}
//        )
//    }
//}