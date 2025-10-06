package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.baseChips
import com.example.tassty.categories
import com.example.tassty.component.CategoryList
import com.example.tassty.component.ChipSearchExpandSection
import com.example.tassty.component.ChipSearchSection
import com.example.tassty.component.CustomFilterChip
import com.example.tassty.component.Divider32
import com.example.tassty.component.EmptyRestaurant
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.FoodNameGridCard
import com.example.tassty.component.HorizontalTitleSection
import com.example.tassty.component.LoadingScreen
import com.example.tassty.component.RestaurantTinyGridCard
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.TitleListHeader
import com.example.tassty.component.TopBarButton
import com.example.tassty.component.restaurantMenuListBlock
import com.example.tassty.historyOptions
import com.example.tassty.menus
import com.example.tassty.model.Category
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.ChipOption
import com.example.tassty.model.DataState
import com.example.tassty.model.FilterState
import com.example.tassty.model.Menu
import com.example.tassty.model.Resource
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.model.SearchUiState
import com.example.tassty.model.StatusState
import com.example.tassty.popularOptions
import com.example.tassty.restaurants
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Orange500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit
) {
    // Access all data ui state
    val status = when (uiState) {
        is SearchUiState.Initial -> uiState.status
        is SearchUiState.Result -> uiState.status
        is SearchUiState.QueryLoading -> uiState.status
        is SearchUiState.ErrorFatal -> uiState.status
    }
    val data = when (uiState) {
        is SearchUiState.Initial -> uiState.data
        is SearchUiState.Result -> uiState.data
        is SearchUiState.QueryLoading -> uiState.data
        is SearchUiState.ErrorFatal -> uiState.data
    }

    // Status if we have result or not
    val isSearchingResult = uiState is SearchUiState.Result || uiState is SearchUiState.QueryLoading

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
                        if (isSearchingResult) {
                            TopBarButton(
                                icon = R.drawable.filter,
                                boxColor = Orange500, iconColor = Neutral10
                            ) { }
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
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            item {
                Column(Modifier.padding(horizontal = 24.dp)) {
                    SearchBarWhiteSection(
                        value = status.queryText,
                        onValueChange = { onQueryChange(status.queryText) }
                    )
                }
            }

            when (uiState) {
                is SearchUiState.Initial -> {
                    item { Spacer(Modifier.height(16.dp)) }

                    // 1. Search History
                    item {
                        ChipListSection(
                            resource = data.history,
                            onRetry = {}
                        )
                    }

                    // 2. Search Popular
                    item {
                        ChipExpandListSection(
                            resource = data.popularSearches,
                            onRetry = {}
                        )
                    }
                    item { Divider32() }

                    // Food category
                    item {
                        CategoryListSection(
                            resource = data.categories,
                            onRetry = {}
                        )
                    }

                    item { Divider32() }

                    // Restaurant section
                    item {
                        RestaurantSection(
                            resource = data.rest,
                            onRetry = {}
                        )
                    }
                    item { Divider32() }

                    // Menu section
                    item {
                        MenuSection(
                            menus = data.menus.data,
                            status = RestaurantStatus.OPEN
                        )
                    }
                }
                is SearchUiState.Result,
                is SearchUiState.QueryLoading-> {
                    item {
                        // Show filter active change label and color
                        Spacer(Modifier.height(16.dp))
                        FilterSection(
                            filterState = status.activeFilters
                        )
                    }
                    item { Divider32() }

                    // Show global loading (shimmer/spinner)
                    if (status.isGlobalLoading) {
                        item { LoadingScreen() }
                    }

                    // Show result
                    val restaurantsToShow = if (data.filterResult.data.isNotEmpty()) {
                        data.filterResult.data // Filter search when active
                    } else {
                        data.queryResult.data // Filter search with query
                    }

                    if (restaurantsToShow.isNotEmpty()) {
                        restaurantMenuListBlock(
                            itemCount = restaurantsToShow.size,
                            headerText = "Search founds",
                            restaurantItems = restaurantsToShow
                        )
                    } else if (status.isGlobalLoading == false) {
                        // Show empty page if nothing found
                        item {
                            TitleListHeader(data = 0, text = "Menu found")
                            EmptyRestaurant()
                        }
                    }
                }
                is SearchUiState.ErrorFatal -> {
                    item {
                        ErrorScreen()
                    }
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
    } else if(resource.data.isNotEmpty()){
        ChipSearchSection(
            title = stringResource(R.string.your_search_history),
            options = resource.data,
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
    } else if(resource.data.isNotEmpty()){
        Divider32()
        ChipSearchExpandSection(
            title = stringResource(R.string.popular_searches),
            options = resource.data,
        )
    }
}

@Composable
fun CategoryListSection(
    resource: Resource<List<Category>>,
    onRetry: () -> Unit
){
    if(resource.errorMessage!=null){
        ErrorListState(title = stringResource(R.string.explore_by_cuisine),
            onRetry = onRetry)
    } else if(resource.data.isNotEmpty()){
        CategoryList(categories = resource.data)
    }
}

@Composable
fun RestaurantSection(
    resource: Resource<List<Restaurant>>,
    onRetry: () -> Unit,
) {
    if(resource.errorMessage!=null){
        ErrorListState(title = stringResource(R.string.restos_you_ve_searched),
            onRetry = onRetry)
    } else if(resource.data.isNotEmpty()) {
        HorizontalTitleSection(
            title = stringResource(R.string.restos_you_ve_searched)
        ) {
            items(
                items = resource.data,
                key = { it.id }
            ) { restaurant ->
                RestaurantTinyGridCard(
                    restaurant = restaurant,
                    status = RestaurantStatus.OPEN
                )
            }
        }
    }
}

@Composable
fun MenuSection(
    menus: List<Menu>,
    status: RestaurantStatus
) {
    HorizontalTitleSection(
        title = stringResource(R.string.menus_you_ve_searched)
    ) {
        items(items = menus,
            key = {it.id}
        ) { menu ->
            FoodNameGridCard(
                menu = menu,
                status = status,
                isFirstItem = false,
                isWishlist = false
            )
        }
    }
}

@Composable
fun FilterSection(
    filterState: FilterState
) {
    val activeChips = buildList {
        // Base chips
        baseChips.forEach { chip ->
            val isActive = when (chip.key) {
                "sort" -> filterState.sort.isNotEmpty()
                "rated" -> filterState.rating.isNotEmpty()
                "promo" -> filterState.promo.isNotEmpty()
                "delivery" -> filterState.delivery.isNotEmpty()
                else -> false
            }

            add(
                chip.copy(
                    label = when (chip.key) {
                        "sort" -> filterState.sort.ifEmpty { chip.label }
                        "rated" -> filterState.rating.ifEmpty { chip.label }
                        "promo" -> filterState.promo.ifEmpty { chip.label }
                        "delivery" -> filterState.delivery.ifEmpty { chip.label }
                        else -> chip.label
                    },
                    isSelected = isActive
                )
            )
        }

        // Extra chips dari Map
        filterState.extras.forEach { (key, value) ->
            if (value.isNotEmpty()) {
                add(
                    ChipOption(
                        key = key,
                        label = value,
                        icon = when (key) {
                            "price" -> R.drawable.promo
                            "cuisines" -> R.drawable.promo
                            else -> null
                        },
                        selectedColor = Orange500,
                        selectedLabelColor = Neutral10,
                        selectedIconColor = Neutral10,
                        selectedBorderColor = Color.Transparent,
                        isSelected = true
                    )
                )
            }
        }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(activeChips) { option ->
            CustomFilterChip(
                option = option
            ) { }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSearchInitialScreen() {
//    SearchScreen(
//        uiState = SearchUiState.Initial(
//            data = DataState(
//                history = Resource(data = historyOptions,isLoading = false),
//                popularSearches = Resource(data = popularOptions,
//                    isLoading = false),
//                categories = Resource(data = categories,
//                    isLoading = false, errorMessage = "Network Error"),
//                rest = Resource(data = restaurants, isLoading = false),
//                menus = Resource(data = menus, isLoading = false)
//            ),
//            status = StatusState(
//                queryText = "", errorMessage = "Network error")
//        ),
//        onQueryChange = {}
//    )
//}
//
@Preview(showBackground = true)
@Composable
fun PreviewSearchErrorScreen() {
    SearchScreen(
        uiState = SearchUiState.ErrorFatal(
            data = DataState(
                history = Resource(data = emptyList(),
                    isLoading = false, errorMessage = "Network Error"),
                popularSearches = Resource(data = emptyList(),
                    isLoading = false, errorMessage = "Network Error"),
                categories = Resource(data = emptyList(),
                    isLoading = false, errorMessage = "Network Error"),
                rest = Resource(data = restaurants, isLoading = false),
                menus = Resource(data = menus, isLoading = false)
            ),
            status = StatusState(
                queryText = "", errorMessage = "Network error"),
            message = "Failed to connect. Please check your internet connection."
        ),
        onQueryChange = {}
    )
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSearchScreen() {
//    SearchScreen(
//        uiState = SearchUiState.Result(
//            data = DataState(
//                queryResult = Resource(data = restaurants),
//            ),
//            status = StatusState(
//                queryText = "burger"
//            )
//        ),
//        onQueryChange = {}
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSearchEmptyScreen() {
//    SearchScreen(
//        uiState = SearchUiState.Result(
//            data = DataState(
//                queryResult = Resource(data = emptyList()),
//            ),
//            status = StatusState(
//                queryText = "burger"
//            )
//        ),
//        onQueryChange = {}
//    )
//}