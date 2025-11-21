package com.example.tassty.screen.category

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.model.RestaurantStatus
import com.example.tassty.component.*
import com.example.tassty.model.FilterState
import com.example.tassty.restaurantUiModel
import com.example.tassty.screen.search.FilterSection
import com.example.tassty.ui.theme.Neutral10

@Composable
fun CategoryScreen(
    categoryName: String,
    imageUrl : String,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val  uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryContent(
        categoryName = categoryName,
        imageUrl = imageUrl,
        uiState = uiState,
        onFilterClick = {viewModel.onEvent(CategoryEvent.ShowFilterSheet)},
        onQueryChange = {viewModel.onEvent(CategoryEvent.onQueryChange(it))}
    )

    CustomBottomSheet(
        visible = uiState.isFilterSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        FilterContent(
            rupiahPriceRanges = uiState.rupiahPriceRanges,
            restoRatingsOptions = uiState.restoRatingsOptions,
            discountOptions = uiState.discountOptions,
            modesOptions = uiState.modesOptions,
            cuisineOption = uiState.cuisineOption,
            onUpdateDraftFilter = {key, value-> viewModel.onEvent(CategoryEvent.UpdateDraftFilter(key,value))},
            onApplyFilter = {viewModel.onEvent(CategoryEvent.ApplyFilters)},
            onResetFilter = {viewModel.onEvent(CategoryEvent.ResetFilter)}
        )
    }
}

@Composable
fun CategoryContent(
    categoryName: String,
    imageUrl : String,
    uiState: CategoryUiState,
    onFilterClick:()-> Unit,
    onQueryChange: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            ScrollableHeaderContent(
                imageUrl = imageUrl,
                status = RestaurantStatus.OPEN,
                category = categoryName,
                onFilterClick = onFilterClick
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 24.dp)
                    .offset(y=-(24).dp)
            ) {
                SearchBarWhiteSection(value = uiState.query,
                    onValueChange = onQueryChange)
            }
        }

        item{
            FilterSection(
                activeSummaryChips = uiState.activeSummaryChips,
                onSortClick = {}
            )
            Divider32()
        }

        filterListSection(
            resource = uiState.queryResult,
            onRetry = {}
        )
    }
}


@Composable
fun ScrollableHeaderContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    status: RestaurantStatus,
    category: String,
    onFilterClick: () -> Unit
) {
    val imageHeight = 304.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        StatusItemImage(
            imageUrl = imageUrl,
            name = "category header image",
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

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
                CategoryAndDescriptionHeader(category = category, imageUrl = imageUrl)
            }
        }

        CategoryTopAppBar(
            onFilterClick = onFilterClick,
            onBackClick = {},
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreenSuccess() {
    CategoryContent(
        categoryName = "Ramen",
        imageUrl = "",
        uiState = CategoryUiState(
            restaurants = restaurantUiModel,
            totalCount = 0,
            activeFilters = FilterState()
        ),
        onFilterClick = {},
        onQueryChange = {}
    )
}
