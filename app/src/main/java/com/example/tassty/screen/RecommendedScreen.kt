package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.categories
import com.example.tassty.component.HorizontalSubtitleListSection
import com.example.tassty.component.RecommendedRestaurantTopAppBar
import com.example.tassty.component.RestaurantLargeGridCard
import com.example.tassty.component.RestaurantLargeListCard
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.TitleListHeader
import com.example.tassty.component.restaurantVerticalListBlock
import com.example.tassty.model.Restaurant // Asumsi model dan data dummy ada
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.restaurants
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral30

// --- FUNGSI UTAMA ---
@Composable
fun RecommendedRestaurantScreen(){
    // Jarak vertikal antar item di dalam LazyColumn
    val verticalSpacing = 32.dp

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Neutral10),
    ){
        // 1. HEADER PENCARIAN & KATEGORI
        item{
            RecommendedRestaurantTopAppBar(onBackClick = {}) { }
        }

        item {
            Spacer(Modifier.height(verticalSpacing))
            SearchHeader()
        }

        item{
            Spacer(Modifier.height(verticalSpacing))
            HorizontalDivider(color= Neutral30)
            Spacer(Modifier.height(verticalSpacing))
        }

        item {
            RecommendedRowContent(
                restaurantItems = restaurants
            )
        }
        
        item{
            Spacer(Modifier.height(verticalSpacing))
            HorizontalDivider(color= Neutral30)
            Spacer(Modifier.height(verticalSpacing))
        }

        restaurantVerticalListBlock(
            headerText = "Recommended Restaurants",
            restaurantItems = restaurants,
            status = RestaurantStatus.OPEN
        )
        
    }
}

// --- FUNGSI SEARCH HEADER ---
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
        // Asumsi CategorySection ada dan menerima List<String> atau sejenisnya
        CategorySection(categoryItems = categories)
    }
}

// --- FUNGSI KONTEN REKOMENDASI (LIST HORIZONTAL) ---
@Composable
fun RecommendedRowContent(
    restaurantItems : List<Restaurant>
){
    // HorizontalListSection harus menerima content slot yang merupakan LazyRow/Row
    HorizontalSubtitleListSection(
        title = "Recommended Restaurant",
        subtitle = "Our recommended cafes to explore!",
        onSeeAllClick = {}
    ) {
        // CONTENT SLOT (LazyRow di dalam HorizontalListSection)
        // Gunakan itemsIndexed/items untuk mengisi LazyRow
        items(
            items = restaurantItems,
            key = { item -> item.id }
        ) { restaurant ->
            RestaurantLargeGridCard(
                restaurant = restaurant,
                status = RestaurantStatus.OPEN
            )
            // Tambahkan padding/spacing di dalam item di LazyRow, bukan di Column luar.
            Spacer(modifier = Modifier.padding(end = 12.dp))
        }
    }
}

@Composable
fun RecommendedListContent(
    restaurantItems : List<Restaurant>
){

}


@Preview(showBackground = true)
@Composable
fun RecommendedScreen() {
    RecommendedRestaurantScreen()
}