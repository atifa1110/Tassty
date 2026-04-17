package com.example.tassty.screen.nearby

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.RestaurantUiModel
import com.example.tassty.R
import com.example.tassty.component.CategoryTopAppBar
import com.example.tassty.component.CustomMarkerDesign
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorListState
import com.example.tassty.component.FilterSection
import com.example.tassty.component.HeaderListItemCountTitle
import com.example.tassty.component.RestaurantLargeListCard
import com.example.tassty.component.RestaurantSmallListCard
import com.example.tassty.component.SearchBar
import com.example.tassty.component.ShimmerRestaurantLargeListCard
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.defaultFilter
import com.example.tassty.util.restaurantUiModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.collections.orEmpty

@Composable
fun NearbyRestaurantScreen(
    onNavigateBack:() -> Unit,
    viewModel: NearbyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NearbyRestaurantContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun NearbyRestaurantContent(
    uiState: NearbyUiState,
    onNavigateBack:() -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapArea(
            modifier = Modifier.fillMaxSize(),
            resource = uiState.resource
        )

        CategoryTopAppBar(
            modifier = Modifier.statusBarsPadding(),
            iconBackground = LocalCustomColors.current.cardBackground,
            onBackClick = onNavigateBack,
            onFilterClick = {}
        )

        DraggableSearchBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            uiState = uiState,
            fixedHeight = maxHeight
        )
    }
}

@Composable
fun DraggableSearchBar(
    modifier: Modifier = Modifier,
    uiState: NearbyUiState,
    minHeight: Dp = 100.dp,
    fixedHeight: Dp
) {
    val density = LocalDensity.current
    var offsetY by remember { mutableFloatStateOf(0f) }

    SubcomposeLayout(modifier = modifier) { constraints ->
        val fixedHeightPx = with(density) { fixedHeight.toPx() }
        val screenHalfHeightPx = fixedHeightPx * 0.6f

        val ghostPlaceable = subcompose("measure") {
            RestaurantContent(uiState = uiState , progress = 1f)
        }.map { it.measure(constraints.copy(minHeight = 0)) }

        val contentHeightPx = ghostPlaceable.maxOfOrNull { it.height } ?: 0
        val maxHeightPx = contentHeightPx.coerceAtMost(screenHalfHeightPx.toInt())
        val minHeightPx = minHeight.roundToPx()
        val totalRangePx = (maxHeightPx - minHeightPx).coerceAtLeast(1).toFloat()
        val progress = (-offsetY / totalRangePx).coerceIn(0f, 1f)
        val currentBoxHeightPx = minHeightPx + (totalRangePx * progress).toInt()

        val contentPlaceable = subcompose("content") {
            val remapProgress = (progress / 0.4f).coerceIn(0f,1f)

            val cornerSize = lerp(99.dp, 24.dp, remapProgress)
            val bottomCorner = lerp(99.dp, 0.dp, remapProgress)
            val horizontalPadding = lerp(16.dp, 0.dp, remapProgress)
            val bottomPadding = lerp(32.dp, 0.dp, remapProgress)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { currentBoxHeightPx.toDp() + 20.dp })
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            offsetY = (offsetY + delta).coerceIn(-totalRangePx, 0f)
                        }
                    )
                    .padding(start = horizontalPadding, end = horizontalPadding, bottom = bottomPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(density) { currentBoxHeightPx.toDp() })
                        .background(
                            color = LocalCustomColors.current.background,
                            shape = RoundedCornerShape(
                                topStart = cornerSize, topEnd = cornerSize,
                                bottomStart = bottomCorner, bottomEnd = bottomCorner
                            )
                        )
                ) {
                    RestaurantContent(uiState = uiState, progress=progress)

                    Box(
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                            .size(width = 36.dp, height = 5.dp)
                            .background(
                                color = LocalCustomColors.current.divider,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            }
        }.map { it.measure(constraints.copy(minHeight = 0)) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlaceable.forEach {
                it.place(0, constraints.maxHeight - (currentBoxHeightPx + with(density) { 20.dp.roundToPx() }))
            }
        }
    }
}

@Composable
fun RestaurantContent(
    uiState: NearbyUiState,
    progress: Float
){
    Column(modifier = Modifier.fillMaxWidth()) {
        val searchPadding = if (progress > 0.2f) {
            PaddingValues(top = 24.dp, start = 24.dp, end = 24.dp)
        } else {
            PaddingValues(16.dp)
        }

        SearchBar(
            modifier = Modifier.padding(searchPadding),
            value = "",
            onValueChange = {},
        )

        AnimatedVisibility(
            modifier = Modifier.padding(top = 16.dp),
            visible = progress > 0.8f,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            FilterSection(
                option = defaultFilter,
                onSortClick = {}
            )
        }

        Divider32()

        HeaderListItemCountTitle(
            modifier = Modifier.padding(horizontal = 24.dp),
            itemCount = uiState.resource.data?.size?:0,
            title = stringResource(R.string.restaurants_nearby)
        )

        AnimatedVisibility(
            visible = progress in 0.3f..0.8f,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            RestaurantHorizontalList(resource = uiState.resource)
        }

        AnimatedVisibility(
            visible = progress > 0.8f,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            RestaurantVerticalList(resource = uiState.resource)
        }
    }
}

@Composable
fun RestaurantHorizontalList(
    resource: Resource<ImmutableList<RestaurantUiModel>>
) {
    val items = resource.data.orEmpty()
    LazyRow(
        modifier = Modifier.padding(top = 12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when{
            resource.isLoading -> {
                items(3) { ShimmerRestaurantLargeListCard() }
            }

            resource.errorMessage != null -> {
                item {
                    ErrorListState(title = stringResource(R.string.restos_nearby_you)) { /* retry */ }
                }
            }

            else -> {
                items(
                    items = items,
                    key = { restaurant -> restaurant.id }
                ) { restaurant ->
                    RestaurantSmallListCard(restaurant = restaurant)
                }
            }
        }
    }
}

@Composable
fun RestaurantVerticalList(
    resource: Resource<ImmutableList<RestaurantUiModel>>,
) {
    val items = resource.data.orEmpty()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 12.dp).fillMaxHeight()
    ) {
        when{
            resource.isLoading -> {
                items(3) { ShimmerRestaurantLargeListCard() }
            }

            resource.errorMessage != null -> {
                item {
                    ErrorListState(title = stringResource(R.string.restos_nearby_you)) { /* retry */ }
                }
            }

            else -> {
                items(
                    items=items,
                    key = { restaurant -> restaurant.id }
                ){ restaurant ->
                    RestaurantLargeListCard(restaurant = restaurant, onClick = {})
                }
            }
        }
    }
}

@Composable
fun MapArea(
    modifier: Modifier = Modifier,
    resource: Resource<ImmutableList<RestaurantUiModel>>
) {
    val items = resource.data.orEmpty()
    val cameraPositionState = rememberCameraPositionState()

    var isMapReady by remember { mutableStateOf(false) }
    LaunchedEffect(resource.isLoading, isMapReady) {
        if (!resource.isLoading && isMapReady && items.isNotEmpty()) {
            val firstRestaurant = items[0].location
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(firstRestaurant.latitude, firstRestaurant.longitude),
                    15f
                )
            )
        }
    }

    Box(
        modifier = modifier.background(LocalCustomColors.current.divider),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    compassEnabled = true,
                ),
                onMapLoaded = { isMapReady = true },
            ) {
                items.forEach { restaurant ->
                    MarkerComposable(
                        state = rememberMarkerState(
                            key = restaurant.id,
                            position = restaurant.location
                        ),
                        title = restaurant.name,
                        anchor = Offset(0.5f, 1f)
                    ) {
                        CustomMarkerDesign(
                            iconRes = R.drawable.store,
                            borderColor = Orange500
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true, name= "Light Mode")
@Composable
fun NearbyLightPreview() {
    TasstyTheme {
        NearbyRestaurantContent(
            uiState = NearbyUiState(
                resource = Resource(restaurantUiModel.toImmutableList())
            ),
            onNavigateBack = {}
        )
    }
}

//@Preview(showBackground = true, name= "Light Mode")
@Composable
fun NearbyDarkPreview() {
    TasstyTheme(darkTheme = true){
        NearbyRestaurantContent(
            uiState = NearbyUiState(
                resource = Resource(restaurantUiModel.toImmutableList())
            ),
            onNavigateBack = {}
        )
    }
}