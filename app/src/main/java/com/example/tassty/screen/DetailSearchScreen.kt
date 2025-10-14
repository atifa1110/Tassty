package com.example.tassty.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.RestaurantStatus
import com.example.tassty.R
import com.example.tassty.component.EmptySearchResult
import com.example.tassty.component.FoodListCard
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.LoadingScreen
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.menus
import com.example.tassty.model.Menu
import com.example.tassty.screen.search.Resource
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Pink500

@Composable
fun DetailSearchScreen(
    query : String,
    status: RestaurantStatus,
    resource : Resource<List<Menu>>,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    val showFullPage = query.isNotEmpty()

    val animatedHeight by animateDpAsState(
        targetValue = if (showFullPage) 1000.dp else 110.dp,
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
                .height(animatedHeight)
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
                    SearchResultList(
                        resource = resource,
                        status = status
                    )
                }
            }
        }
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
                if(searchQuery.isNotEmpty()){
                    onQueryChange("")
                }else {
                    onClose()
                }
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
    resource: Resource<List<Menu>>,
    status: RestaurantStatus
) {
    val menuItems = resource.data.orEmpty()

    when{
        resource.isLoading -> {
            LoadingScreen()
        }
        resource.errorMessage!=null || menuItems.isEmpty() ->{
            EmptySearchResult(title = "Menu found")
        }
        else ->{
            Column(modifier =Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeaderListItemCountTitle(
                    itemCount = menuItems.size,
                    title = "Menu found",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(menuItems) { item ->
                        FoodListCard(item,
                            status = status,
                            false,
                            onFavoriteClick = {}
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearch(){
    DetailSearchScreen(
        query = "burger",
        resource = Resource(
            data = menus
        ),
        status = RestaurantStatus.OPEN,
        onQueryChange = {},
        onClose = {}
    )
}