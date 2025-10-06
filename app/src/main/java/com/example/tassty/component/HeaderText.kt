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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.tassty.categoriesItem
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun HeaderText(
    text: String,
    textColor : Color,
    textButton: String,
    onButtonClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = textColor,
            style = LocalCustomTypography.current.h5Bold
        )
        Text(
            modifier = Modifier.clickable{
                onButtonClick()
            },
            text = textButton,
            color = Orange500,
            style = LocalCustomTypography.current.bodyMediumMedium
        )
    }

}

@Composable
fun HeaderSubtitleText(
    title: String,
    subtitle:String,
    titleColor : Color,
    subtitleColor: Color,
    textButton: String,
    onButtonClick:() -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = titleColor,
                style = LocalCustomTypography.current.h5Bold
            )
            Text(
                text = subtitle,
                color = subtitleColor,
                style = LocalCustomTypography.current.bodySmallRegular
            )
        }
        Text(
            modifier = Modifier.clickable{
                onButtonClick()
            },
            text = textButton,
            color = Orange500,
            style = LocalCustomTypography.current.bodyMediumMedium
        )
    }

}

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
fun TextHeader(
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
        TextHeader(title = category, subtitle = "Some lunch boosters!")
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
        TextHeader(title = "Our best seller!",
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