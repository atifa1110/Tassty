package com.example.tassty.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.tassty.component.CategoryTopAppBar
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.restaurants
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.RestaurantLargeListCard
import com.example.tassty.component.RestaurantSmallListCard
import com.example.tassty.model.FilterState
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.restaurantUiModel
import com.example.tassty.screen.search.FilterSection

@Composable
fun NearbyRestaurantScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapArea(modifier = Modifier.fillMaxSize())

        CategoryTopAppBar(
            onBackClick = {}, onFilterClick = {}
        )

        // Draggable Search Bar
        DraggableSearchBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            status = RestaurantStatus.OPEN)
    }
}

@Composable
fun DraggableSearchBar(
    modifier: Modifier = Modifier,
    status: RestaurantStatus,
    bottomSpacing: Dp = 32.dp,
    minHeight: Dp = 80.dp,
    maxHeight: Dp = 600.dp
) {
    var offsetY by remember { mutableStateOf(0f) }

    val dynamicHeight by animateDpAsState(
        targetValue = (minHeight - offsetY.dp).coerceIn(minHeight, maxHeight),
        label = "heightAnim"
    )

    val dynamicBottomPadding by animateDpAsState(
        targetValue = if (offsetY >= 0f) bottomSpacing else 0.dp,
        label = "bottomPaddingAnim"
    )
    val dynamicHorizontalPadding by animateDpAsState(
        targetValue = if (offsetY >= 0f) 16.dp else 0.dp,
        label = "horizontalPaddingAnim"
    )

    val dynamicSearchPadding by animateDpAsState(
        targetValue = if (dynamicHeight > minHeight * 2)
            24.dp else 12.dp,
        label = "horizontalPaddingAnim"
    )

    // === Animasi sudut dinamis ===
    val topCorner by animateDpAsState(
        targetValue = if (dynamicHeight > minHeight * 2) 24.dp else 99.dp,
        label = "topCornerAnim"
    )

    val bottomCorner by animateDpAsState(
        targetValue = if (dynamicHeight > minHeight * 2) 0.dp else 99.dp,
        label = "bottomCornerAnim"
    )

    Box(modifier = modifier
            .padding(
                bottom = dynamicBottomPadding,
                start = dynamicHorizontalPadding,
                end = dynamicHorizontalPadding
            )
            .fillMaxWidth()
            .height(dynamicHeight)
    ) {
        Column(modifier = Modifier.fillMaxSize()
                .background(
                    color = Color.White.copy(0.7f),
                    shape = RoundedCornerShape(
                        topStart = topCorner,
                        topEnd = topCorner,
                        bottomStart = bottomCorner,
                        bottomEnd = bottomCorner
                    )
                )
        ) {
            Column(Modifier.padding(dynamicSearchPadding)) {
                SearchBarWhiteSection(
                    value = "",
                    onValueChange = {},
                    placeholder = "Search",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            AnimatedVisibility(
                modifier = Modifier.padding(),
                visible = dynamicHeight >= 400.dp,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                FilterSection(filterState = FilterState())
            }

            if (dynamicHeight >= 400.dp) {
                Spacer(Modifier.height(32.dp))
            } else {
                Spacer(Modifier.height(8.dp))
            }

            HorizontalDivider(color = Color(0xFFDEDEDE))
            Spacer(Modifier.height(32.dp))

            HeaderListItemCountTitle(
                itemCount = restaurants.size,
                title = "Restaurants nearby"
            )

            AnimatedVisibility(
                modifier = Modifier.padding(),
                visible = dynamicHeight < 250.dp,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(restaurantUiModel) { restaurant ->
                    RestaurantSmallListCard(restaurant = restaurant)
                }
            }
        }

            AnimatedVisibility(
                modifier = Modifier.padding(),
                visible = dynamicHeight >= 400.dp,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // kasih jarak antar item
                ) {
                    items(
                        items = restaurantUiModel,
                        key = { it.restaurant.id }
                    ) { restaurant ->
                        RestaurantLargeListCard(restaurant = restaurant)
                    }
                }
            }
        }

        // Handle drag
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-5).dp)
                .size(width = 40.dp, height = 6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Gray.copy(alpha = 0.6f))
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        offsetY += delta // update offset
                    }
                )
        )
    }
}


@Composable
fun MapArea(modifier: Modifier) {
    Box(
        modifier = modifier.background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        // Dummy Map background
        Text("Map Area")
    }
}


@Preview
@Composable
fun PreviewNearbyScreen() {
    NearbyRestaurantScreen()
}


