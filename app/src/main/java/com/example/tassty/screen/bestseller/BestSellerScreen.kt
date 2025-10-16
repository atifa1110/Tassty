package com.example.tassty.screen.bestseller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.domain.model.RestaurantStatus
import com.example.tassty.component.BestSellerHeader
import com.example.tassty.component.CategoryTopAppBar
import com.example.tassty.component.FoodWideListCard
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.StatusItemImage
import com.example.tassty.menus
import com.example.tassty.screen.detailrestaurant.ShoppingCartBottomBar
import com.example.tassty.ui.theme.Neutral10

@Composable
fun BestSellerScreen() {
    Scaffold (
        containerColor = Neutral10,
        bottomBar = {
            ShoppingCartBottomBar(
                itemCount = 4,
                totalPrice = 25000,
                onCartClick = {}
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().background(Neutral10),
        ) {
            item {
                ScrollableBestHeaderContent(
                    imageUrl = "https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format",
                    status = RestaurantStatus.OPEN,
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth().padding(horizontal = 24.dp)
                        .offset(y=-(24).dp)
                ) {
                    SearchBarWhiteSection(value = "", onValueChange = {})
                }
            }

            items(items = menus, key ={it.menu.id} ) { item ->
                Column(Modifier.padding(horizontal = 24.dp)) {
                    FoodWideListCard(menu = item)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ScrollableBestHeaderContent(
    imageUrl: String,
    status: RestaurantStatus,
    modifier: Modifier = Modifier
) {
    val imageHeight = 304.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        // A. Header Image with status overlay
        StatusItemImage(
            imageUrl = imageUrl,
            name = "category header image",
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

        // B. Category card overlay at bottom of header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = Neutral10.copy(0.9f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp,
                        end = 24.dp, top = 24.dp,
                        bottom = 48.dp)
            ) {
                BestSellerHeader()
            }
        }

        CategoryTopAppBar(
            onFilterClick = {},
            onBackClick = {},
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BestSellerPreview(){
    BestSellerScreen()
}