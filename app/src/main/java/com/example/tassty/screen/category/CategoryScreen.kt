package com.example.tassty.screen.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.RestaurantStatus
import com.example.tassty.R
import com.example.tassty.component.*
import com.example.tassty.model.FilterState
import com.example.tassty.restaurantUiModel
import com.example.tassty.screen.search.FilterSection
import com.example.tassty.ui.theme.Neutral10
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    category: String,
    uiState: CategoryUiState,
) {
    val context = LocalContext.current

    // Extract active filters from uiState
    val activeFilters = when (uiState) {
        is CategoryUiState.Success -> uiState.activeFilters
        is CategoryUiState.Error -> uiState.activeFilters
        is CategoryUiState.Loading -> FilterState()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            // 1. Header Image with Parallax effect
            ScrollableHeaderContent(
                imageUrl = "https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format",
                status = RestaurantStatus.OPEN,
                category = category
            )
        }
        // Sticky search bar and filters
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 24.dp)
                    .offset(y=-(24).dp)
            ) {
                SearchBarWhiteSection(value = "", onValueChange = {})
            }
        }

        item{
            FilterSection(filterState = activeFilters)
            Divider32()
        }

        // Content based on uiState
        when (uiState) {
            is CategoryUiState.Loading -> item { LoadingScreen() }
            is CategoryUiState.Error -> item { ErrorScreen() }
            is CategoryUiState.Success -> {
                if (uiState.restaurants.isNotEmpty()) {
                    restaurantMenuListBlock(
                        itemCount = uiState.totalCount,
                        headerText = context.getString(R.string.restos_that_have, category),
                        restaurantItems = uiState.restaurants
                    )
                } else {
                    item {
                        EmptySearchResult(title = context.getString(R.string.restos_that_have, category))
                    }
                }
            }
        }
    }
}

@Composable
fun ScrollableHeaderContent(
    imageUrl: String,
    status: RestaurantStatus,
    category: String,
    modifier: Modifier = Modifier
) {
    val imageHeight = 304.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        // A. Header Image with status overlay
        ItemImage(
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
                CategoryAndDescriptionHeader(category = category)
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
fun PreviewCategoryScreenSuccess() {
    CategoryScreen(
        category = "Ramen",
        uiState = CategoryUiState.Success(
            restaurants = restaurantUiModel,
            totalCount = 0,
            activeFilters = FilterState(sort = "Nearest")
        )
    )
}
