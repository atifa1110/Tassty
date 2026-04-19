package com.example.tassty.screen.orderprocess

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.data.source.remote.network.Resource
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.RouteOrderUiModel
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.CompletedContent
import com.example.tassty.component.CustomBottomSheet
import com.example.tassty.component.CustomMarkerDesign
import com.example.tassty.component.DebitSmallPaymentCard
import com.example.tassty.component.DeliveryDetailCard
import com.example.tassty.component.DeliveryDriverCard
import com.example.tassty.component.DeliveryLocationCard
import com.example.tassty.component.Divider
import com.example.tassty.component.Divider32
import com.example.tassty.component.OrderStatusCard
import com.example.tassty.component.OrderStepProgress
import com.example.tassty.component.OrderSummaryCard
import com.example.tassty.screen.rating.HeaderIconText
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.OrderPreviewData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun OrderProcessScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMessage : (String) -> Unit,
    viewModel: OrderProcessViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.events.collect { event->
            when(event){
                is OrderProcessEvent.NavigateToMessage -> {
                    onNavigateToMessage(event.channelId)
                }
                is OrderProcessEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                OrderProcessEvent.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    OrderProcessContent(
        uiState = uiState,
        onMessageClick = viewModel::onChatClicked,
        onNavigateBack = onNavigateBack
    )

    CustomBottomSheet(
        visible = uiState.isArrivedModalVisible,
        dismissOnClickOutside = false,
        onDismiss = {}
    ) {
        CompletedContent(
            time = 25,
            onDismissClick = viewModel::onDismissModal,
            onRatingDriver = {}
        )
    }
}

@Composable
fun OrderProcessContent(
    uiState: OrderProcessUiState,
    onNavigateBack:() -> Unit,
    onMessageClick:() -> Unit
) {

    val order = uiState.detail.data?: return
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapArea(
            uiState = uiState
        )

        BackTopAppBar(onBackClick = onNavigateBack)

        DraggableOrderProcess(
            modifier = Modifier.align(Alignment.BottomCenter),
            detail = order,
            onMessageClick = onMessageClick
        )
    }
}

@Composable
fun MapArea(
    uiState: OrderProcessUiState,
    modifier: Modifier = Modifier,
) {
    val order = uiState.detail.data ?: return
    val route = uiState.route.data?: return

    val allPoints = route.polylinePoints
    val currentIndex = uiState.currentStepIndex

    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(route.polylinePoints) {
        if (route.polylinePoints.isNotEmpty()) {
            val builder = LatLngBounds.Builder()
            route.polylinePoints.forEach { builder.include(it) }

            val bounds = builder.build()
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, 300)
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    ) {
        Polyline(points = allPoints, color = Neutral60, width = 10f)

        if (currentIndex != null) {
            val progressPoints = allPoints.subList(
                0, (currentIndex + 1).coerceAtMost(allPoints.size)
            )
            Polyline(points = progressPoints, color = Blue500, width = 12f, zIndex = 1f)

            uiState.driverPosition?.let { position ->
                key (position) {
                    val markerState = rememberMarkerState(position = position)

                    LaunchedEffect(position) {
                        markerState.position = position
                    }
                    MarkerComposable(
                        state = markerState,
                        anchor = Offset(0.5f, 0.5f),
                        zIndex = 2f
                    ) {
                        CustomMarkerDesign(iconRes = R.drawable.ic_delivery,
                            borderColor = Blue500)
                    }
                }
            }
        }

        MarkerComposable(state = rememberMarkerState(position = order.restaurant.location)) {
            CustomMarkerDesign(iconRes = R.drawable.store, borderColor = Pink500)
        }
        MarkerComposable(state = rememberMarkerState(position = order.userAddress.location)) {
            CustomMarkerDesign(iconRes = R.drawable.person, borderColor = Orange500)
        }
    }
}

@Composable
fun DraggableOrderProcess(
    detail: DetailOrderUiModel,
    modifier: Modifier = Modifier,
    minHeight: Dp = 340.dp,
    maxHeight: Dp = 600.dp,
    onMessageClick : () -> Unit,
) {
    val density = LocalDensity.current
    val maxHeightPx = with(density) { maxHeight.toPx() }
    val minHeightPx = with(density) { minHeight.toPx() }

    var currentHeightPx by remember { mutableFloatStateOf(minHeightPx) }

    val expandProgress = ((currentHeightPx - minHeightPx) / (maxHeightPx - minHeightPx)).coerceIn(0f, 1f)

    val animatedHeight by animateDpAsState(
        targetValue = with(density) { currentHeightPx.toDp() },
        label = "heightAnim"
    )

    val cornerSize by animateDpAsState(
        targetValue = lerp(28.dp, 0.dp, expandProgress),
        label = "cornerAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .clip(RoundedCornerShape(topStart = cornerSize, topEnd = cornerSize))
            .background(LocalCustomColors.current.cardBackground)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()
                    currentHeightPx = (currentHeightPx - dragAmount).coerceIn(minHeightPx, maxHeightPx)
                }
            }
    ) {
        Column (modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(Neutral40, CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "${detail.restaurant.name} Order - ${detail.queueNumber}",
                    style = LocalCustomTypography.current.h3Bold,
                    color = LocalCustomColors.current.headerText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeaderIconText(
                        text = detail.orderNumber,
                        style = LocalCustomTypography.current.bodySmallRegular,
                    )
                    Text(
                        text = "•",
                        style = LocalCustomTypography.current.bodySmallRegular,
                        color = Neutral40
                    )
                    HeaderIconText(
                        icon = R.drawable.clock,
                        iconColor = Blue500,
                        text = detail.createdAt,
                        style = LocalCustomTypography.current.bodySmallRegular,
                    )
                }
            }

            Divider32()

            OrderStatusCard(
                currentStatus = detail.status
            )

            OrderStepProgress(
                currentStatus = detail.status,
                modifier = Modifier.padding(top = 20.dp)
            )

            Divider(modifier = Modifier.padding(top = 32.dp))
            if (expandProgress > 0.5f) {
                OrderProcessExpandedContent(detail ,expandProgress,onMessageClick)
            }
        }
    }
}

@Composable
fun OrderProcessExpandedContent(
    detail: DetailOrderUiModel,
    expandProgress: Float,
    onMessageClick:() -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
            .alpha(expandProgress)
            .verticalScroll(rememberScrollState())
    ) {
        DeliveryDriverCard(
            driver = detail.driver,
            onMessageClick = onMessageClick
        )
        Divider32()
        DeliveryLocationCard(
            restaurant = detail.restaurant,
            userAddress = detail.userAddress
        )
        Divider32()
        DeliveryDetailCard(detail.orderItems)
        Spacer(Modifier.height(12.dp))
        Column (Modifier.padding(horizontal = 24.dp)){
            OrderSummaryCard(
                isPercentageDiscount = false,
                totalPrice = detail.totalPrice,
                deliveryFee = detail.deliveryFee,
                voucherDiscount = detail.discount,
                totalOrder = detail.finalAmount
            )
            Spacer(Modifier.height(12.dp))
            DebitSmallPaymentCard(
                card = detail.cardPayment
            )
        }
    }
}

//@Preview(showBackground = true, name = "Light Mode")
@Composable
fun OrderProcessLightPreview(){
    TasstyTheme{
        OrderProcessContent(
            uiState = OrderProcessUiState(
                detail = Resource(data = OrderPreviewData.detailOrder),
                route = Resource(
                    data = RouteOrderUiModel(
                        distance = "", duration = "", polylinePoints = emptyList(), status = ""
                    )
                ),
                currentStepIndex = 0,
                isSimulated = true,
            ),
            onMessageClick = {},
            onNavigateBack = {}
        )
    }
}