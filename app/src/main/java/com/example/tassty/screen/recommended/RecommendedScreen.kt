package com.example.tassty.screen.recommended

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.R
import com.example.tassty.util.categories
import com.example.tassty.component.CategoryCard
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.HorizontalTitleSubtitleSection
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.RestaurantLargeGridCard
import com.example.tassty.component.SearchBar
import com.example.tassty.component.ShimmerFoodGridCard
import com.example.tassty.component.ShimmerRestaurantGridCard
import com.example.tassty.component.TitleTopAppBar
import com.example.tassty.component.restaurantRecommendedSection
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.restaurantUiModel
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun RecommendedRestaurantScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailRest:(String) -> Unit,
    viewModel: RecommendedViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RecommendedRestaurantContent(
        uiState = uiState,
        onCategoryClick = viewModel::onCategoryClicked,
        onNavigateToDetailRest = onNavigateToDetailRest,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RecommendedRestaurantContent(
    uiState: RecommendedUiState,
    onNavigateBack: () -> Unit,
    onCategoryClick:(String)-> Unit,
    onNavigateToDetailRest:(String) -> Unit,
){
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            TitleTopAppBar(
                title = stringResource(R.string.recommended_restaurant),
                onBackClick = onNavigateBack,
                onFilterClick = {}
            )
        }
    ) { padding->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            item(key = "search_header") {
                Spacer(Modifier.height(24.dp))
                SearchHeader(
                    resource = uiState.allCategories,
                    onClick = onCategoryClick
                )
            }

            item(key = "recommended_rest"){
                RecommendedCategoryContent(
                    resource = uiState.recommendedRestaurantCategories,
                    onNavigateToDetail = onNavigateToDetailRest
                )
            }

            item {
                Divider32()
            }

            restaurantRecommendedSection(
                resource = uiState.recommendedRestaurant,
                onNavigateToDetail = onNavigateToDetailRest
            )
        }
    }
}

@Composable
fun SearchHeader(
    resource: Resource<List<CategoryUiModel>>,
    onClick: (String) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Column( modifier = Modifier.padding(horizontal = 24.dp)) {
            SearchBar(
                value = "",
                onValueChange = {}
            )
        }
        CategorySection(
            resource = resource,
            onClick = onClick
        )
    }
}

@Composable
fun CategorySection(
    resource: Resource<List<CategoryUiModel>>,
    onClick: (String) -> Unit,
){
    val items = resource.data.orEmpty()

    when {
        resource.isLoading -> {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(8){
                    Spacer(modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .shimmerLoadingAnimation()
                    )
                }
            }
        }

        resource.errorMessage!= null || items.isEmpty() -> {
            ErrorListState("") { }
        }

        else ->{
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items (items = items, key = { it.id }) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { onClick(category.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedCategoryContent(
    resource: Resource<List<RestaurantUiModel>>,
    onNavigateToDetail: (String) -> Unit
) {
    val items = resource.data.orEmpty()
    val shouldShowSection = resource.isLoading || items.isNotEmpty()

    AnimatedVisibility(
        visible = shouldShowSection,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            Divider32()
            HorizontalTitleSubtitleSection(
                title = stringResource(R.string.recommended_restaurant),
                subtitle = stringResource(R.string.our_recommended_places_to_explore),
                onSeeAllClick = {}
            ) {
                when {
                    resource.isLoading -> {
                        items(5) {
                            ShimmerRestaurantGridCard()
                        }
                    }
                    resource.errorMessage != null -> {
                        item {
                            ErrorScreen()
                        }
                    }
                    else -> {
                        items(items = items, key = { it.id }) { restaurant ->
                            RestaurantLargeGridCard(
                                restaurant = restaurant,
                                onClick = { onNavigateToDetail(restaurant.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun RecommendedLightPreview() {
    TasstyTheme {
        RecommendedRestaurantContent(
            uiState = RecommendedUiState(
                selectedCategoryId = "CAT-001",
                allCategories = Resource(data = categories),
                recommendedRestaurant = Resource(data = restaurantUiModel),
                recommendedRestaurantCategories = Resource(data = restaurantUiModel)
            ),
            onCategoryClick = {},
            onNavigateBack = {},
            onNavigateToDetailRest = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun RecommendedDArkPreview() {
    TasstyTheme(darkTheme = true) {
        RecommendedRestaurantContent(
            uiState = RecommendedUiState(
                selectedCategoryId = "CAT-001",
                allCategories = Resource(data = categories),
                recommendedRestaurant = Resource(data = restaurantUiModel),
                recommendedRestaurantCategories = Resource(data = restaurantUiModel)
            ),
            onCategoryClick = {},
            onNavigateBack = {},
            onNavigateToDetailRest = {}
        )
    }
}