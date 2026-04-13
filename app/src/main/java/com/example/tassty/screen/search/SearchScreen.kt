package com.example.tassty.screen.search

import android.content.Context
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.categories
import com.example.tassty.util.defaultFilter
import com.example.tassty.util.historyOptions
import com.example.tassty.util.menusItem
import com.example.tassty.util.popularOptions
import com.example.tassty.util.restaurantMenuUiModel
import com.example.tassty.util.restaurantUiModel

@Composable
fun SearchScreen(
    onNavigateBack:() -> Unit,
    onNavigateToDetail:(String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchRoute(
        context = context,
        uiState = uiState,
        onQueryChange = {viewModel.onEvent(SearchEvent.ChangeQuery(it))},
        onFilterClick = {viewModel.onEvent(SearchEvent.ShowFilterSheet)},
        onSortClick = {viewModel.onEvent(SearchEvent.ShowSortSheet)},
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
fun SearchRoute(
    context: Context,
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onSortClick: () -> Unit,
    onNavigateToDetail:(String) -> Unit,
    onNavigateBack:() -> Unit,
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = LocalCustomColors.current.background
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.search),
                            style = LocalCustomTypography.current.h5Bold,
                            color = LocalCustomColors.current.headerText
                        )
                    },
                    navigationIcon = {
                        TopBarButton(
                            icon = R.drawable.arrow_left,
                            boxColor = LocalCustomColors.current.topBarBackgroundColor,
                            iconColor = LocalCustomColors.current.iconFocused,
                            onClick = onNavigateBack
                        )
                    },
                    actions = {
                        if (uiState.isSearching) {
                            TopBarButton(
                                icon = R.drawable.filter,
                                boxColor = Orange500,
                                iconColor = Neutral10,
                                onClick = onFilterClick
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(top = 12.dp,bottom = 24.dp),
        ) {
            item(key = "search_bar") {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    SearchBar(
                        value = uiState.query,
                        onValueChange = { onQueryChange(it) },
                        isTransparentMode = false
                    )
                }
            }

            item (key = "search_content"){
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
                    context = context,
                    resource = uiState.queryResult,
                    onNavigateToDetail = onNavigateToDetail
                )
            } else {
                item(key = "search_history") {
                    Spacer(Modifier.height(16.dp))
                    ChipListSection(
                        resource = uiState.history,
                        onRetry = {}
                    )
                }
                item(key = "search_popular") {
                    ChipExpandListSection(
                        resource = uiState.popular,
                        onRetry = {}
                    )
                    Divider32()
                }
                item(key = "category") {
                    CategoryListSection(
                        resource = uiState.categories,
                        onRetry = {}
                    )
                    Divider32()
                }
                item(key = "restaurant_section") {
                    RestaurantSection(
                        resource = uiState.restaurants,
                        onRetry = {}
                    )
                    Divider32()
                }
                item(key = "menu_section") {
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

        else -> {
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
            ErrorListState(
                title = stringResource(R.string.restos_you_ve_searched),
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

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SearchLightPreview() {
    TasstyTheme {
        SearchRoute(
            context = LocalContext.current,
            uiState = SearchUiState(
                history = Resource(data = historyOptions),
                popular = Resource(data = popularOptions),
                categories = Resource(data = categories),
                restaurants = Resource(data = restaurantUiModel),
                menus = Resource(data = menusItem),
                activeFilters = defaultFilter,
                isSearching = true,
                query = "cafe",
                queryResult = Resource(restaurantMenuUiModel)
            ),
            onQueryChange = {},
            onFilterClick = {},
            onSortClick = {},
            onNavigateToDetail = {},
            onNavigateBack = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun SearchDarkPreview() {
    TasstyTheme(darkTheme = true){
        SearchRoute(
            context = LocalContext.current,
            uiState = SearchUiState(
                history = Resource(data = historyOptions),
                popular = Resource(data = popularOptions),
                categories = Resource(data = categories),
                restaurants = Resource(data = restaurantUiModel),
                menus = Resource(data = menusItem),
                activeFilters = defaultFilter,
                isSearching = true,
                query = "cafe",
                queryResult = Resource(restaurantMenuUiModel, errorMessage = "error")
            ),
            onQueryChange = {},
            onFilterClick = {},
            onSortClick = {},
            onNavigateToDetail = {},
            onNavigateBack = {}
        )
    }
}