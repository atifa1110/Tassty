package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.component.CategoryAndDescriptionHeader
import com.example.tassty.component.FilterList
import com.example.tassty.component.FoodTinyGridCard
import com.example.tassty.component.HeaderWithOverlap
import com.example.tassty.component.RestaurantLargeListCard
import com.example.tassty.component.restaurantMenuListBlock
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.restaurants
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral30

@Composable
fun CategoryScreen(
    restaurants: List<Restaurant>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Neutral10),
    ) {
        // 1. Header Section
        item {
            HeaderWithOverlap(
                imageUrl = "",
                status = RestaurantStatus.OPEN,
                onBackClick = {},
                onFilterClick = {},
                headerContent = {
                    CategoryAndDescriptionHeader()
                }
            )
        }
        // 2. Filter/Sort Section
        item {
            Spacer(modifier = Modifier.height(48.dp))
            FilterList()
        }

        item{
            HorizontalDivider(color = Neutral30, modifier = Modifier.padding(vertical = 32.dp))
        }

        restaurantMenuListBlock(
            headerText = "Restos that have martabaks",
            restaurantItems = restaurants
        )
    }
}

@Composable
fun RestaurantContentSection(
    restaurant: Restaurant,
    status: RestaurantStatus
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Column (Modifier.padding(horizontal = 24.dp)){
            RestaurantLargeListCard(restaurant = restaurant, status = status)
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(restaurant.menus, key = { it.id }) { menuItem ->
                FoodTinyGridCard(menu = menuItem,status = status)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
    CategoryScreen(
        restaurants = restaurants
    )
}