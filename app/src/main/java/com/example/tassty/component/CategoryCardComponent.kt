package com.example.tassty.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import coil.compose.AsyncImage
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500

@Composable
fun FoodCategoryCard(
    isSelected: Boolean,
    imageUrl: String,
    categoryName: String,
    onCardClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) Orange50 else Neutral20
    val borderColor = if (isSelected) Orange500 else Color.Transparent

    Card(
        modifier = modifier.size(120.dp).clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)).clickable{
                onCardClick()
            } ,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = categoryName,
                    style = LocalCustomTypography.current.h6Bold,
                    color = Neutral100
                )
            }

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
                .offset(x = 10.dp, y = (-10).dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.martabak),
                    contentDescription = "Deskripsi gambar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(86.dp).clip(CircleShape)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodCategoryCard() {
    FoodCategoryCard(
        imageUrl = "https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format", // Contoh URL gambar
        categoryName = "Salad",
        isSelected = false,
        onCardClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodCategoryCardSelected() {
    FoodCategoryCard(
        imageUrl = "https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format", // Contoh URL gambar
        categoryName = "Salad",
        isSelected = true,
        onCardClick = {}
    )
}