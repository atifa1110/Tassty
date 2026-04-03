package com.example.tassty.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun CategoryFoundHeader(
    searchQuery: String,
    filteredCategories: List<CategoryUiModel>
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
                .copy(color = LocalCustomColors.current.headerText)
        ) {
            append(headerText)
        }
    }

    Text(text = styledText)
}

@Composable
fun StatusModalContent(
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
            color = LocalCustomColors.current.headerText,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = subtitle,
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = LocalCustomColors.current.text,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun StatusTwoColorModalContent(
    title : String,
    subtitle : String = "You can’t undo this action.",
    highlight: String = "?",
    titleColor : Color = LocalCustomColors.current.headerText,
    highlightColor: Color = Pink500,
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CustomTwoColorText(
            fullText = title,
            textColor = titleColor,
            highlightText = highlight,
            highlightColor = highlightColor,
            normalStyle = LocalCustomTypography.current.h2Bold,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = subtitle,
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = LocalCustomColors.current.text,
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
                color = LocalCustomColors.current.headerText,
                style = LocalCustomTypography.current.h5Bold
            )
            Text(
                text = subtitle,
                color = LocalCustomColors.current.headerText,
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
            color = LocalCustomColors.current.headerText
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
            withStyle(style = LocalCustomTypography.current.h5Bold.toSpanStyle().copy(color = LocalCustomColors.current.headerText)) {
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

@Composable
fun HeaderRestaurantCollection(
    modifier: Modifier = Modifier,
    restaurantName: String,
    rating: String,
    city: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = restaurantName,
            style = LocalCustomTypography.current.h6Bold,
            color = LocalCustomColors.current.headerText,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = true)
        )

        Text(
            text = " • ",
            color = LocalCustomColors.current.text,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Orange500,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = rating,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = LocalCustomColors.current.text,
            modifier = Modifier.padding(start = 4.dp)
        )

        Text(
            text = " • ",
            color = Neutral70,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Icon(
            painter = painterResource(R.drawable.location),
            contentDescription = null,
            tint = Pink500,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = city,
            style = LocalCustomTypography.current.bodySmallMedium,
            color = LocalCustomColors.current.text,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(Modifier.weight(0.3f))

        Text(
            text = "See resto",
            style = LocalCustomTypography.current.bodySmallMedium,
            color = Orange500,
            modifier = Modifier.padding(start = 12.dp).clickable(
                onClick = onClick
            )
        )
    }
}

@Composable
fun HeaderTitleScreen(
    modifier: Modifier = Modifier,
    title: String
){
    CustomTwoColorText(
        modifier = modifier,
        fullText = title,
        highlightText = ".",
        textColor = LocalCustomColors.current.headerText,
        normalStyle = LocalCustomTypography.current.h2Bold,
        textAlign = TextAlign.Start
    )
}

@Composable
fun DateHeader(date: String) {
    Box(modifier = Modifier.fillMaxWidth()
        .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Neutral70.copy(alpha = 0.8f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = date,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = Neutral10,
                style = LocalCustomTypography.current.bodyXtraSmallMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHeader(){
    Column (Modifier.fillMaxWidth().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)){

        HeaderTitleScreen(
            modifier = Modifier.fillMaxWidth(),
            title = "My collections."
        )

        HeaderListTitleButton(
            title = "Recommended Restaurants",
            titleColor = Neutral100,
            onClick = {}
        )

        HeaderListTitleSubtitleButton(
            title = "Recommended Restaurants",
            subtitle = "Our recommended cafes to explore!",
            onClick = {}
        )

        HeaderListBlackTitle(
            title = "Suggested menus for you!"
        )

        HeaderListItemCountTitle(
            itemCount = 24,
            title = "Search founds"
        )

        HeaderListItemCountTitleButton(
            itemCount = 24,
            title = "Search founds",
            onClick = {}
        )

        HeaderRestaurantCollection(
            restaurantName = "Indomie Upnormal Habatusausa›",
            rating = "4.9",
            city = "Jakarta Selatan"
        )
    }
}