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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    headerContent: @Composable () -> Unit
) {
    // Definisikan nilai overlap untuk konsistensi di seluruh layout
    val imageHeight = 304.dp
    val searchBarOverlapHeight = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            // Tinggi Box hanya setinggi gambar (304.dp)
            .height(imageHeight)
    ) {
        // LAYER 1: Gambar Latar Belakang
        ItemImage(
            imageUrl = imageUrl,
            name = "category header image",
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

        // LAYER 2: Top App Bar
        CategoryTopAppBar(onBackClick = onBackClick, onFilterClick = onFilterClick)

        // --- LAYER 3: Header Frame Card (Container untuk Konten Header) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
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
                // SLOT DENGAN KONTEN YANG BISA BERUBAH
                headerContent()
            }
        }

        // --- LAYER 4: Search Bar (Floating & Overlap) ---
        SearchBarWhiteSection(
            value = "", // Anda mungkin ingin SearchBar juga menjadi parameter jika nilainya berubah
            onValueChange = {}, // Sama seperti di atas
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                // Offset NEGATIF untuk menarik Search Bar ke atas dan mengisi padding Card
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
fun CategoryAndDescriptionHeader(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextHeader(title = "Ramen", subtitle = "Some lunch boosters!")
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