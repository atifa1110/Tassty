package com.example.tassty.screen.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.R
import com.example.tassty.categories
import com.example.tassty.component.CategoryList
import com.example.tassty.component.ChipSearchExpandSection
import com.example.tassty.component.ChipSearchSection
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.FilterContent
import com.example.tassty.component.FilterSection
import com.example.tassty.component.FoodNameGridCard
import com.example.tassty.component.HorizontalTitleSection
import com.example.tassty.component.RestaurantTinyGridCard
import com.example.tassty.component.SearchBar
import com.example.tassty.component.ShimmerCategoryStaggeredList
import com.example.tassty.component.ShimmerHorizontalTitleButtonSection
import com.example.tassty.component.ShimmerRestaurantTinyGridCard
import com.example.tassty.component.SortContent
import com.example.tassty.component.TopBarButton
import com.example.tassty.component.filterListSection
import com.example.tassty.defaultFilter
import com.example.tassty.historyOptions
import com.example.tassty.menusItem
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.popularOptions
import com.example.tassty.restaurantUiModel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Orange500

@Composable
fun SearchRoute(
    onNavigateToDetail:(String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState,
        onQueryChange = {viewModel.onEvent(SearchEvent.ChangeQuery(it))},
        onFilterClick = {viewModel.onEvent(SearchEvent.ShowFilterSheet)},
        onSortClick = {viewModel.onEvent(SearchEvent.ShowSortSheet)},
        onNavigateToDetail = onNavigateToDetail
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
            onUpdateDraftFilter = {category,key -> viewModel.onEvent(SearchEvent.UpdateDraftFilter(category,key))},
            onApplyFilter = {viewModel.onEvent(SearchEvent.ApplyFilters)},
            onResetFilter = {viewModel.onEvent(SearchEvent.ResetFilter)}
        )
    }

    CustomBottomSheet(
        visible = uiState.isSortSheetVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        SortContent(
            sortList = uiState.sortList,
            onUpdateDraftFilter = {category , key->viewModel.onEvent(SearchEvent.UpdateDraftFilter(category,key))},
            onApplySort = {viewModel.onEvent(SearchEvent.ApplySort)},
            onResetSort = {viewModel.onEvent(SearchEvent.ResetSort)}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onNavigateToDetail:(String) -> Unit
) {
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Neutral10
                    ),
                    title = {
                        Text(
                            text = "Search",
                            style = LocalCustomTypography.current.h5Bold,
                            color = Neutral100
                        )
                    },
                    navigationIcon = {
                        TopBarButton(
                            icon = R.drawable.arrow_left,
                            boxColor = Neutral20, iconColor = Neutral100
                        ) { }
                    },
                    actions = {
                        // show button if filter is search is active
                        if (uiState.isSearching) {
                            TopBarButton(
                                icon = R.drawable.filter,
                                boxColor = Orange500, iconColor = Neutral10
                            ) { onFilterClick() }
                        }
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Neutral10),
            contentPadding = PaddingValues(top = 12.dp,bottom = 24.dp),
        ) {
            item {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    SearchBar(
                        value = uiState.query,
                        onValueChange = { onQueryChange(it) },
                        isTransparentMode = false
                    )
                }
            }

            item {
                AnimatedContent(targetState = uiState.isSearching) { isSearching ->
                    Column {
                        if (isSearching) {
                            Spacer(Modifier.height(12.dp))
                            FilterSection (
                                option = uiState.activeFilters,
                                onSortClick = onSortClick
                            )
                            Divider32()
                        }
                    }
                }
            }

            if (uiState.isSearching) {
                filterListSection(
                    resource = uiState.queryResult,
                    onNavigateToDetail = onNavigateToDetail
                )
            } else {
                item {
                    Spacer(Modifier.height(16.dp))
                    ChipListSection(
                        resource = uiState.history,
                        onRetry = {}
                    )

                    ChipExpandListSection(
                        resource = uiState.popular,
                        onRetry = {}
                    )
                    Divider32()

                    CategoryListSection(
                        resource = uiState.categories,
                        onRetry = {}
                    )

                    Divider32()

                    RestaurantSection(
                        resource = uiState.restaurants,
                        onRetry = {}
                    )

                    Divider32()

                    MenuSection(
                        resource = uiState.menus,
                        onRetry = {}
                    )
                }
            }
        }
    }
}

@Composable
fun ChipListSection(
    resource: Resource<List<ChipFilterOption>>,
    onRetry: () -> Unit
){
    if(resource.errorMessage!=null){
        ErrorListState(title = stringResource(R.string.your_search_history),
            onRetry = onRetry)
    } else if(resource.data!=null){
        ChipSearchSection(
            title = stringResource(R.string.your_search_history),
            options = resource.data?:emptyList(),
            selected = false
        )
    }
}

@Composable
fun ChipExpandListSection(
    resource: Resource<List<ChipFilterOption>>,
    onRetry: () -> Unit
){
    if(resource.errorMessage!=null){
        ErrorListState(title = stringResource(R.string.popular_searches),
            onRetry = onRetry)
    } else if(resource.data!=null){
        Divider32()
        ChipSearchExpandSection(
            title = stringResource(R.string.popular_searches),
            options = resource.data?:emptyList(),
        )
    }
}

@Composable
fun CategoryListSection(
    resource: Resource<List<CategoryUiModel>>,
    onRetry: () -> Unit
){
    val items = resource.data.orEmpty()
    when{
        resource.isLoading->{
            ShimmerCategoryStaggeredList()
        }

        resource.errorMessage!=null || items.isEmpty() ->{
            ErrorListState(
                title = stringResource(R.string.explore_by_cuisine),
                onRetry = onRetry
            )
        }

        else ->{
            CategoryList(categories = items)
        }
    }
}

@Composable
fun RestaurantSection(
    resource: Resource<List<RestaurantUiModel>>,
    onRetry: () -> Unit,
) {
    val items = resource.data.orEmpty()
    when{
        resource.isLoading->{
            ShimmerHorizontalTitleButtonSection {
                items(4){
                    ShimmerRestaurantTinyGridCard()
                }
            }
        }
        resource.errorMessage!=null || items.isEmpty()-> {
            ErrorListState(title = stringResource(R.string.restos_you_ve_searched),
                onRetry = onRetry)
        }
        else ->{
            HorizontalTitleSection(
                title = stringResource(R.string.restos_you_ve_searched)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { restaurant ->
                    RestaurantTinyGridCard(
                        restaurant = restaurant
                    )
                }
            }
        }
    }
}

@Composable
fun MenuSection(
    resource: Resource<List<MenuUiModel>>,
    onRetry: () -> Unit,
) {
    val items = resource.data.orEmpty()
    when {
        resource.isLoading -> {
            ShimmerHorizontalTitleButtonSection {
                items(4) {
                    ShimmerRestaurantTinyGridCard()
                }
            }
        }

        resource.errorMessage != null || items.isEmpty() -> {
            ErrorListState(
                title = stringResource(R.string.menus_you_ve_searched),
                onRetry = onRetry
            )
        }

        else -> {
            HorizontalTitleSection(
                title = stringResource(R.string.menus_you_ve_searched)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { menu ->
                    FoodNameGridCard(
                        menu = menu
                    )
                }
            }
        }
    }
}

//@Composable
//fun FilterSection(
//    options : List<FilterOptionUi>,
//    onSortClick: () -> Unit
//) {
//    LazyRow(
//        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(horizontal = 24.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(options) { option ->
//            CustomFilterChip(
//                option = option,
//                onClick = {
//                    if(option.category == FilterCategory.SORT){
//                        onSortClick()
//                    }
//                }
//            )
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun PreviewSearchInitialScreen() {
    SearchScreen(
        uiState = SearchUiState(
            history = Resource(data = historyOptions,isLoading = false),
            popular = Resource(data = popularOptions,
                isLoading = false),
            categories = Resource(data = categories,
                isLoading = false, errorMessage = "Network Error"),
            restaurants = Resource(data = restaurantUiModel, isLoading = false),
            menus = Resource(data = menusItem, isLoading = false),
            activeFilters = defaultFilter,
        ),
        onQueryChange = {},
        onFilterClick = {},
        onSortClick = {},
        onNavigateToDetail = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchErrorScreen() {
    SearchScreen(
        uiState = SearchUiState(
            history = Resource(data = historyOptions,isLoading = false),
            popular = Resource(data = popularOptions,
                isLoading = false),
            categories = Resource(data = categories,
                isLoading = false, errorMessage = "Network Error"),
            restaurants = Resource(data = restaurantUiModel, isLoading = false),
            menus = Resource(data = menusItem, isLoading = false),
            activeFilters = defaultFilter,
            query = "burger"
        ),
        onQueryChange = {},
        onFilterClick = {},
        onSortClick = {},
        onNavigateToDetail = {}
    )
}