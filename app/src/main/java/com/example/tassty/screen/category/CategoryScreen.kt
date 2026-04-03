package com.example.tassty.screen.category

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.data.source.remote.network.Resource
import com.example.core.domain.model.RestaurantStatus
import com.example.tassty.component.*
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.util.defaultFilter
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.restaurantMenuUiModel

@Composable
fun CategoryScreen(
    name: String,
    image: String,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryContent(
        categoryName = name,
        imageUrl = image,
        uiState = uiState,
        onFilterClick = {viewModel.onEvent(CategoryEvent.ShowFilterSheet)},
        onSortClick = {viewModel.onEvent(CategoryEvent.ShowSortSheet)},
        onQueryChange = {viewModel.onEvent(CategoryEvent.onQueryChange(it))},
        onNavigateToDetail = onNavigateToDetail,
        onNavigateBack = onNavigateBack
    )

    CustomBottomSheet(
        visible = uiState.isFilterSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        FilterContent(
            rupiahPriceRanges = uiState.priceRanges,
            restoRatingsOptions = uiState.ratingsOptions,
            modesOptions = uiState.modesOptions,
            cuisineOption = uiState.cuisineOptions,
            onUpdateDraftFilter = {category,key-> viewModel.onEvent(CategoryEvent.UpdateDraftFilter(category,key))},
            onApplyFilter = {viewModel.onEvent(CategoryEvent.ApplyFilters)},
            onResetFilter = {viewModel.onEvent(CategoryEvent.ResetFilter)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isSortSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        SortContent(
            sortList = uiState.sortList,
            onUpdateDraftFilter = {category, key-> viewModel.onEvent(CategoryEvent.UpdateDraftFilter(category,key))},
            onApplySort = {viewModel.onEvent(CategoryEvent.ApplySort)},
            onResetSort = {viewModel.onEvent(CategoryEvent.ResetSort)}
        )
    }
}

@Composable
fun CategoryContent(
    categoryName: String,
    imageUrl : String,
    uiState: CategoryUiState,
    onFilterClick:()-> Unit,
    onSortClick:()-> Unit,
    onQueryChange: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 200
        }
    }

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween (300),
        label = "alpha"
    )

    val iconBackground by animateColorAsState(
        targetValue = if (isScrolled) LocalCustomColors.current.cardBackground else LocalCustomColors.current.topBarBackgroundColor,
        animationSpec = tween(300),
        label = "iconTint"
    )

    Scaffold (
        containerColor = LocalCustomColors.current.background
    ){ paddingValues ->
        BoxWithConstraints(modifier = Modifier.padding(paddingValues)
            .fillMaxSize()) {
            val screenHeight = maxHeight
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item(key = "header_content") {
                    HeaderContent(
                        fixedHeight = screenHeight,
                        imageUrl = imageUrl,
                        category = categoryName,
                        query = uiState.query,
                        onQueryChange = onQueryChange
                    )
                    Spacer(Modifier.height(24.dp))
                }

                item(key = "filter_content") {
                    FilterSection(
                        option = uiState.activeFilters,
                        onSortClick = onSortClick
                    )
                    Divider32()
                }

                filterListSection(
                    resource = uiState.restaurants,
                    onNavigateToDetail = onNavigateToDetail
                )
            }

            CategoryTopAppBar(
                iconBackground = iconBackground,
                onFilterClick = onFilterClick,
                onBackClick = onNavigateBack,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(
                        LocalCustomColors.current.background.copy(alpha = appBarAlpha)
                    )
            )
        }
    }
}


@Composable
fun HeaderContent(
    fixedHeight: Dp,
    imageUrl: String,
    category: String,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val imageHeight = fixedHeight * 0.4f
    val searchBarHeight = 56.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight + (searchBarHeight / 2))
    ) {
        CommonImage(
            imageUrl = imageUrl,
            name = "category header image",
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight * 0.45f)
                .align(Alignment.BottomCenter)
                .padding(bottom = searchBarHeight / 2)
                .background(
                    color = LocalCustomColors.current.modalBackgroundFrame.copy(0.9f)
                )
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            CategoryHeader(title = category, imageUrl = imageUrl)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(searchBarHeight)
                .align(Alignment.BottomCenter)
        ) {
            SearchBar(
                value = query,
                onValueChange = onQueryChange
            )
        }
    }
}

@Composable
fun CategoryHeader(
    title: String,
    imageUrl: String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = title,
                style = LocalCustomTypography.current.h3Bold,
                color = LocalCustomColors.current.headerText
            )

            Text(
                text = "Some lunch boosters!",
                style = LocalCustomTypography.current.bodySmallMedium,
                color = LocalCustomColors.current.text
            )
        }

        CategoryCard(title = title, image = imageUrl, onClick = {})
    }
}


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun CategoryLightPreview() {
//    TasstyTheme {
//        CategoryContent(
//            categoryName = "Ramen",
//            imageUrl = "",
//            uiState = CategoryUiState(
//                restaurants = Resource(data = restaurantMenuUiModel),
//                activeFilters = defaultFilter
//            ),
//            onFilterClick = {},
//            onSortClick = {},
//            onQueryChange = {},
//            onNavigateToDetail = {},
//            onNavigateBack = {}
//        )
//    }
//}
//
//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun CategoryDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        CategoryContent(
//            categoryName = "Ramen",
//            imageUrl = "",
//            uiState = CategoryUiState(
//                restaurants = Resource(data = restaurantMenuUiModel),
//                activeFilters = defaultFilter
//            ),
//            onFilterClick = {},
//            onSortClick = {},
//            onQueryChange = {},
//            onNavigateToDetail = {},
//            onNavigateBack = {}
//        )
//    }
//}
