package com.example.tassty.screen.recommended

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
import com.example.tassty.categories
import com.example.tassty.component.CategoryCard
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.HorizontalTitleSubtitleSection
import com.example.tassty.component.RestaurantLargeGridCard
import com.example.tassty.component.SearchBarWhiteSection
import com.example.tassty.component.TitleTopAppBar
import com.example.tassty.component.restaurantRecommendedSection
import com.example.tassty.component.restaurantVerticalListBlock
import com.example.tassty.component.shimmerLoadingAnimation
import com.example.tassty.restaurantUiModel
import com.example.tassty.screen.home.CategorySection
import com.example.tassty.ui.theme.Neutral10

@Composable
fun RecommendedRestaurantScreen(
    onNavigateToDetail:(String) -> Unit,
    viewModel: RecommendedViewModel = hiltViewModel()
){
    val uiState by viewModel.recommendedState.collectAsStateWithLifecycle()

    RecommendedRestaurantContent(
        uiState =uiState,
        onCategoryClick = {viewModel.onCategoryClicked(it)},
        onNavigateToDetail = onNavigateToDetail
    )
}

@Composable
fun RecommendedRestaurantContent(
    uiState: RecommendedUiState,
    onCategoryClick:(String)-> Unit,
    onNavigateToDetail:(String) -> Unit,
){
    Scaffold(
        containerColor = Neutral10,
        topBar = {
            TitleTopAppBar(
                title = stringResource(R.string.recommended_restaurant),
                onBackClick = {}) { }
        }
    ) { padding->
        LazyColumn(modifier = Modifier.padding(padding)
            .fillMaxSize()
            .background(Neutral10),
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                SearchHeader(
                    resource = uiState.allCategories, onClick = onCategoryClick)
            }

            item {
                Divider32()
            }

            item {
                RecommendedCategoryContent(
                    resource = uiState.recommendedRestaurantCategories,
                    onNavigateToDetail = onNavigateToDetail
                )
            }

            item {
                Divider32()
            }

            restaurantRecommendedSection(
                resource = uiState.recommendedRestaurant,
                onNavigateToDetail = onNavigateToDetail
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
            SearchBarWhiteSection(
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
    when{
        resource.isLoading -> {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(8){
                    Spacer(modifier = Modifier.size(50.dp).clip(CircleShape)
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
){
    val items = resource.data.orEmpty()
    HorizontalTitleSubtitleSection(
        title = "Recommended Restaurant",
        subtitle = "Our recommended cafes to explore!",
        onSeeAllClick = {}
    ) {
        items(
            items = items,
            key = { item -> item.id }
        ) { restaurant ->
            RestaurantLargeGridCard(
                restaurant = restaurant,
                onClick = {onNavigateToDetail(restaurant.id)}
            )
            Spacer(modifier = Modifier.padding(end = 12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendedPreview() {
    RecommendedRestaurantContent(
        uiState = RecommendedUiState(
            selectedCategoryId = "CAT-001",
            allCategories = Resource(data = categories),
            recommendedRestaurant = Resource(data = restaurantUiModel),
            recommendedRestaurantCategories = Resource(data = restaurantUiModel)
        ),
        onCategoryClick = {}
    ) { }
}