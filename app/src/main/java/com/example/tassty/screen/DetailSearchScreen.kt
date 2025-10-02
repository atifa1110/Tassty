package com.example.tassty.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.FoodListCard
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.TitleListHeader
import com.example.tassty.menus
import com.example.tassty.model.Menu
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Pink500

@Composable
fun DetailSearchScreen(
    query : String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    val showFullPage = query.isNotEmpty()

    val animatedHeight by animateDpAsState(
        targetValue = if (showFullPage) 1000.dp else 110.dp, // misal 64dp = tinggi bar
        label = "searchHeight"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(animatedHeight) // animasi tinggi
                .background(Color.White)
        ) {
            // SearchBar di atas
            SearchAppBarActive(
                searchQuery = query,
                onQueryChange = onQueryChange,
                onClose = onClose
            )

            // Isi muncul kalau query ga kosong
            AnimatedVisibility(visible = showFullPage) {
                Column {
                    HorizontalDivider(Modifier.padding(vertical = 32.dp))

                    val filteredMenus = menus.filter {
                        it.name.contains(query, ignoreCase = true)
                    }

                    TitleListHeader(
                        data = filteredMenus.size,
                        text = "Menu found"
                    )

                    Spacer(Modifier.height(12.dp))

                    if (filteredMenus.isEmpty()) {
                        EmptyRestaurant()
                    } else {
                        SearchResultList(
                            menus = filteredMenus, status = RestaurantStatus.OPEN
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyRestaurant(){
    Column (
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp,end=24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.empty_search),
            contentDescription = "Empty Search Restaurant",
        )

        Spacer(Modifier.height(12.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "We couldn’t find \nthis menu.",
            style = LocalCustomTypography.current.h2Bold,
            color = Neutral100
        )

        Spacer(Modifier.height(10.dp))

        Text(
            textAlign = TextAlign.Center,
            text = "Try a different search keyword or look for your \nfavorite menu at other restaurants.",
            style = LocalCustomTypography.current.bodyMediumRegular,
            color = Neutral70
        )
    }

}

@Composable
fun SearchAppBarActive(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
        .fillMaxWidth().background(Neutral10.copy(0.9f))
        .padding(start =24.dp, end = 24.dp, top = 24.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        SearchBarWhiteSection(
            value = searchQuery,
            onValueChange = {
                onQueryChange(it)
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier.size(44.dp).background(Neutral10).clickable{
                onClose()
            },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.x),
                contentDescription = "Close Search",
                tint = Pink500,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
fun SearchResultList(
    menus: List<Menu>,
    status: RestaurantStatus
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menus) { item ->
                FoodListCard(item, status = status,false,false)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearch(){
    DetailSearchScreen(query = "burger",
        onQueryChange = {"burger"}, onClose = {}
    )
}