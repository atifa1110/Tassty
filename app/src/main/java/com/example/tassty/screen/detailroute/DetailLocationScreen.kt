package com.example.tassty.screen.detailroute

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.model.RestaurantLocationArgs
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.CustomMarkerDesign
import com.example.tassty.component.RestaurantShortInfoCard
import com.example.tassty.component.StatusItemImage
import com.example.tassty.component.TopBarButton
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.TasstyTheme
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun DetailLocationScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailLocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        viewModel.cameraEvents.collect { update ->
            cameraPositionState.animate(update)
        }
    }

    DetailLocationContent(
        uiState = uiState,
        cameraPositionState = cameraPositionState,
        onBackClick = onNavigateBack,
        onZoomOut = viewModel::onZoomOut,
        onZoomIn = viewModel::onZoomIn,
        onMapLoaded = viewModel::updateCameraToMarkers
    )
}

@Composable
fun DetailLocationContent(
    cameraPositionState: CameraPositionState,
    uiState: DetailLocationUiState,
    onBackClick: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onMapLoaded: (LatLng?, LatLng) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapArea(
            uiState = uiState,
            cameraPositionState = cameraPositionState,
            onZoomIn = onZoomIn,
            onZoomOut = onZoomOut,
            onMapLoaded = onMapLoaded
        )

        BackTopAppBar(onBackClick = onBackClick)

        DraggableRestaurantBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            uiState = uiState
        )
    }
}

@Composable
fun MapArea(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    uiState: DetailLocationUiState,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onMapLoaded: (LatLng?, LatLng) -> Unit,
) {
    var hasAutoZoomed by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.userLatLng, hasAutoZoomed) {
        if (uiState.userLatLng != null && hasAutoZoomed) {
            onMapLoaded(uiState.userLatLng, uiState.restaurant.location)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true,
                mapToolbarEnabled = true
            ),
            onMapLoaded = { hasAutoZoomed = true },
        ) {
            MarkerComposable(
                state = rememberMarkerState(position = uiState.restaurant.location),
                title = uiState.restaurant.name,
                anchor = Offset(0.5f, 1f)
            ) {
                CustomMarkerDesign(
                    iconRes = R.drawable.store,
                    borderColor = Orange500
                )
            }

            uiState.userLatLng?.let {
                MarkerComposable(
                    state = rememberMarkerState(position = it),
                    title = "Your Location",
                    anchor = Offset(0.5f, 1f)
                ) {
                    CustomMarkerDesign(
                        iconRes = R.drawable.person
                    )
                }
            }

            val polyline = uiState.route.data?.polylinePoints
            if (!polyline.isNullOrEmpty()) {
                Polyline(
                    points = polyline,
                    color = Orange500,
                    width = 12f,
                    jointType = JointType.ROUND,
                    startCap = RoundCap(),
                    endCap = RoundCap()
                )

                uiState.routeCenterPoint?.let { centerPos ->
                    MarkerComposable(
                        state = rememberMarkerState(position = centerPos),
                        anchor = Offset(0.5f, 0.5f)
                    ) {
                        Surface (
                            color = Color(0xFFF44336),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = uiState.route.data?.distance ?: "0 km",
                                color = Color.White,
                                style = LocalCustomTypography.current.bodyMediumRegular,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TopBarButton(
                icon = R.drawable.add,
                boxColor = LocalCustomColors.current.topBarBackgroundColor,
                iconColor = Orange500,
                onClick = onZoomIn
            )

            TopBarButton(
                icon = Icons.Default.Remove,
                boxColor = LocalCustomColors.current.topBarBackgroundColor,
                iconColor = Orange500,
                onClick = onZoomOut
            )
        }
    }
}

@Composable
fun DraggableRestaurantBar(
    modifier: Modifier = Modifier,
    uiState: DetailLocationUiState,
    minHeight: Dp = 108.dp
) {
    val density = LocalDensity.current
    var offsetY by remember { mutableFloatStateOf(0f) }

    SubcomposeLayout(modifier = modifier) { constraints ->
        val ghostPlaceable = subcompose("measure") {
            RestaurantContent(uiState, progress = 1f)
        }.map { it.measure(constraints.copy(minHeight = 0)) }

        val maxHeightPx = ghostPlaceable.maxOfOrNull { it.height } ?: 0
        val minHeightPx = minHeight.roundToPx()

        val totalRangePx = (maxHeightPx - minHeightPx).coerceAtLeast(1).toFloat()
        val progress = (-offsetY / totalRangePx).coerceIn(0f, 1f)

        val currentBoxHeightPx = minHeightPx + (totalRangePx * progress).toInt()

        val contentPlaceable = subcompose("content") {
            val cornerSize = lerp(99.dp, 24.dp, progress)
            val bottomCorner = lerp(99.dp, 0.dp, progress)

            val horizontalPadding = lerp(24.dp, 0.dp, progress)
            val bottomPadding = lerp(30.dp, 0.dp, progress)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { currentBoxHeightPx.toDp() })
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { delta ->
                            offsetY = (offsetY + delta).coerceIn(-totalRangePx, 0f)
                        }
                    ).padding(start = horizontalPadding, end = horizontalPadding, bottom = bottomPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(density) { currentBoxHeightPx.toDp() })
                        .background(
                            color = LocalCustomColors.current.cardBackground,
                            shape = RoundedCornerShape(
                                topStart = cornerSize, topEnd = cornerSize,
                                bottomStart = bottomCorner, bottomEnd = bottomCorner
                            )
                        )
                ) {
                    RestaurantContent(uiState, progress)

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
        }.map { it.measure(constraints.copy(minHeight = 0, maxHeight = currentBoxHeightPx)) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlaceable.forEach { it.place(0, constraints.maxHeight - currentBoxHeightPx) }
        }
    }
}

@Composable
private fun RestaurantContent(
    uiState: DetailLocationUiState,
    progress: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusItemImage(
                imageUrl = uiState.restaurant.imageUrl,
                status = RestaurantStatus.OPEN,
                name = uiState.restaurant.name,
                modifier = Modifier
                    .size(44.dp)
                    .clip(if (progress > 0.5f) RoundedCornerShape(10.dp) else CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.restaurant.name,
                    style = if (progress > 0.5f) LocalCustomTypography.current.h3Bold else LocalCustomTypography.current.h5Bold,
                    color = LocalCustomColors.current.headerText
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = uiState.restaurant.city,
                    style = LocalCustomTypography.current.bodySmallMedium,
                    color = LocalCustomColors.current.text
                )
            }

            if (progress > 0.5f) {
                TopBarButton(
                    icon = R.drawable.phone,
                    boxColor = Orange500,
                    iconColor = Neutral10
                ) { }
            }
        }

        if (progress > 0.5f) {
            HorizontalDivider(
                modifier = Modifier.padding(top =  8.dp),
                color = LocalCustomColors.current.divider
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .graphicsLayer { alpha = (progress - 0.5f) * 2f }
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = null,
                        tint = Pink500
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = uiState.restaurant.fullAddress,
                        style = LocalCustomTypography.current.bodyMediumRegular,
                        color = LocalCustomColors.current.text
                    )
                }
                Spacer(Modifier.height(16.dp))
                RestaurantShortInfoCard(
                    rating = uiState.restaurant.rating,
                    totalReviews = uiState.restaurant.totalReviews,
                    todayHour = uiState.restaurant.todayHour
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

//@Preview(showBackground = true, name="Light Mode")
@Composable
fun DetailLocationLightPreview() {
    val cameraPositionState = rememberCameraPositionState()
    TasstyTheme {
        DetailLocationContent(
            uiState = DetailLocationUiState(
                restaurant = RestaurantLocationArgs(
                    id="RES-001",
                    name = "Restaurant",
                    imageUrl = "",
                    totalReviews = "(200+)",
                    rating = "4.8",
                    fullAddress = "jalan kelima nomer 2",
                    isVerified = true,
                    todayHour = "08.00 - 10.00",
                    city = "Depok",
                    location = LatLng(0.0,0.0)
                ),
            ),
            cameraPositionState =cameraPositionState,
            onBackClick = {},
            onZoomOut = {},
            onZoomIn = {},
            onMapLoaded = {_,_->}
        )
    }
}