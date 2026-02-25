package com.example.tassty.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.R
import com.example.tassty.component.EmptyFavoriteContent
import com.example.tassty.component.FavoriteTopAppBar
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.TopBarButton
import com.example.tassty.component.restaurantVerticalListBlock
import com.example.tassty.restaurantUiModel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Pink500
import kotlin.collections.orEmpty

@Composable
fun FavoriteScreen(
    onNavigateBack: () -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }

    FavoriteContent(
        resource = uiState.resource,
        snackHostState = snackHostState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun FavoriteContent(
    resource: Resource<List<RestaurantUiModel>>,
    snackHostState: SnackbarHostState,
    onNavigateBack:() -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackHostState) },
        containerColor = Neutral10,
        topBar = {
            FavoriteTopAppBar(
                onBackClick = onNavigateBack
            ) { }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item{
                Column(
                    modifier = Modifier.fillMaxWidth().background(Neutral20)
                        .padding(vertical = 24.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Favorite Restaurants",
                            style = LocalCustomTypography.current.h3Bold,
                            color = Neutral100
                        )
                        TopBarButton(
                            icon = R.drawable.store,
                            boxColor = Pink500, iconColor = Neutral10
                        ) { }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            favoriteRestaurantSection(
                resource = resource
            )
        }
    }
}

fun LazyListScope.favoriteRestaurantSection(
    resource: Resource<List<RestaurantUiModel>>
){
    val items = resource.data.orEmpty()
    when{
        resource.isLoading ->{
            item{
                LoadingRowState()
            }
        }
        resource.errorMessage!=null || items.isEmpty()->{
            item {
                HeaderListItemCountTitle(
                    itemCount = items.size,
                    title = "Favorite restaurants",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                EmptyFavoriteContent()
            }
        }
        else -> {
            restaurantVerticalListBlock(
                headerText = "Favorite restaurants",
                restaurantItems = items,
                onNavigateToDetail = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteReview() {
    val snackHostState = remember { SnackbarHostState() }
    FavoriteContent(
        resource = Resource(data = restaurantUiModel),
        snackHostState = snackHostState,
        onNavigateBack = {}
    )
}