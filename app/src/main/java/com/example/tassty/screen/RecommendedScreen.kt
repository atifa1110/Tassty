package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.categories
import com.example.tassty.component.Divider32
import com.example.tassty.component.HorizontalSubtitleListSection
import com.example.tassty.component.RestaurantLargeGridCard
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.TitleTopAppBar
import com.example.tassty.component.restaurantVerticalListBlock
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.restaurants
import com.example.tassty.ui.theme.Neutral10

@Composable
fun RecommendedRestaurantScreen(){
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            TitleTopAppBar(
                title = stringResource(R.string.recommended_restaurant),
                onBackClick = {}) { }
        }
    ) { padding->
        LazyColumn(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(Neutral10),
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                SearchHeader()
            }

            item {
                Divider32()
            }

            item {
                RecommendedRowContent(
                    restaurantItems = restaurants
                )
            }

            item {
                Divider32()
            }

            restaurantVerticalListBlock(
                headerText = "Recommended Restaurants",
                restaurantItems = restaurants,
                status = RestaurantStatus.OPEN
            )

        }
    }
}

@Composable
fun SearchHeader(){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Column( modifier = Modifier.padding(horizontal = 24.dp)) {
            SearchBarWhiteSection(
                value = "",
                onValueChange = {}
            )
        }
        CategorySection(categoryItems = categories)
    }
}

@Composable
fun RecommendedRowContent(
    restaurantItems : List<Restaurant>
){
    HorizontalSubtitleListSection(
        title = "Recommended Restaurant",
        subtitle = "Our recommended cafes to explore!",
        onSeeAllClick = {}
    ) {
        items(
            items = restaurantItems,
            key = { item -> item.id }
        ) { restaurant ->
            RestaurantLargeGridCard(
                restaurant = restaurant,
                status = RestaurantStatus.OPEN
            )
            Spacer(modifier = Modifier.padding(end = 12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendedScreen() {
    RecommendedRestaurantScreen()
}