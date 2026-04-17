package com.example.tassty.screen.favorite

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.R
import com.example.tassty.component.CommonImage
import com.example.tassty.component.EmptyFavoriteContent
import com.example.tassty.component.FavoriteTopAppBar
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.LoadingRowState
import com.example.tassty.component.TopBarButton
import com.example.tassty.component.restaurantVerticalListBlock
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.restaurantUiModel
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.TasstyTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavoriteScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRecommended: () -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }

    FavoriteContent(
        items = uiState.resource,
        snackHostState = snackHostState,
        onNavigateBack = onNavigateBack,
        onNavigateToRecommended = onNavigateToRecommended
    )
}

@Composable
fun FavoriteContent(
    items: ImmutableList<RestaurantUiModel>?,
    snackHostState: SnackbarHostState,
    onNavigateBack:() -> Unit,
    onNavigateToRecommended: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 ||
                    scrollState.firstVisibleItemScrollOffset > 100
        }
    }

    val iconBackground by animateColorAsState(
        targetValue = if (isScrolled) LocalCustomColors.current.cardBackground else LocalCustomColors.current.topBarBackgroundColor,
        animationSpec = tween(300),
        label = "iconBackground"
    )

    val appBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 1f else 0f,
        animationSpec = tween(300),
        label = "alpha"
    )
    val background = LocalCustomColors.current.background

    Scaffold(
        snackbarHost = { SnackbarHost(snackHostState) },
        containerColor = background
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()) {
            val screenHeight = maxHeight

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    HeaderContent(
                        imageUrl = "https://www.byblos.com/wp-content/uploads/Restaurant-IL-Giardino_Hotel-Byblos_Saint-Tropez-©Stephan-Julliard-7-1600x1000.jpg",
                        fixedHeight = screenHeight
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                favoriteRestaurantSection(
                    items = items,
                    onNavigateToRecommended = onNavigateToRecommended
                )
            }

            FavoriteTopAppBar(
                iconBackground = iconBackground,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .drawBehind {
                        drawRect(color = background.copy(alpha = appBarAlpha))
                    },
                onBackClick = onNavigateBack,
                onSearchClick = {}
            )
        }
    }
}

@Composable
fun HeaderContent(
    fixedHeight: Dp,
    imageUrl: String
){
    val imageHeight = fixedHeight * 0.3f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        CommonImage(
            imageUrl = imageUrl,
            name = "detail header image",
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .background(LocalCustomColors.current.modalBackgroundFrame),
            verticalArrangement = Arrangement.Center
        ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.favorite_restaurants),
                        style = LocalCustomTypography.current.h3Bold,
                        color = LocalCustomColors.current.headerText
                    )
                    TopBarButton(
                        icon = R.drawable.store,
                        boxColor = Pink500, iconColor = Neutral10,
                        onClick = {}
                    )
                }
        }
    }
}

fun LazyListScope.favoriteRestaurantSection(
    items: ImmutableList<RestaurantUiModel>?,
    onNavigateToRecommended: () -> Unit,
){
    if (items == null) {
        return
    }

    if(items.isEmpty()) {
        item(key = "empty_content") {
            EmptyFavoriteContent(
                onClick = onNavigateToRecommended
            )
        }
    }else {
        restaurantVerticalListBlock(
            restaurantItems = items,
            onNavigateToDetail = {}
        )
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun FavoriteLightReview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme {
        FavoriteContent(
            items = restaurantUiModel.toImmutableList(),
            snackHostState = snackHostState,
            onNavigateBack = {},
            onNavigateToRecommended = {}
        )
    }
}

//@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun FavoriteDarkReview() {
    val snackHostState = remember { SnackbarHostState() }
    TasstyTheme (darkTheme = true){
        FavoriteContent(
            items = restaurantUiModel.toImmutableList(),
            snackHostState = snackHostState,
            onNavigateBack = {},
            onNavigateToRecommended = {}
        )
    }
}