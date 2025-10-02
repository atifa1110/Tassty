package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.component.BestSellerHeader
import com.example.tassty.component.FoodWideListCard
import com.example.tassty.component.HeaderWithOverlap
import com.example.tassty.menus
import com.example.tassty.model.RestaurantStatus
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
            // 1. Header Section
            item {
                HeaderWithOverlap(
                    imageUrl = "",
                    status = RestaurantStatus.OPEN,
                    onBackClick = {},
                    onFilterClick = {},
                    headerContent = {
                        BestSellerHeader()
                    }
                )
            }

            item {
                Spacer(Modifier.height(48.dp))
            }

            items(items = menus, key ={it.id} ) { item ->
                Column(Modifier.padding(horizontal = 24.dp)) {
                    FoodWideListCard(item, status = RestaurantStatus.OPEN,false, false)
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BestSellerPreview(){
    BestSellerScreen()
}