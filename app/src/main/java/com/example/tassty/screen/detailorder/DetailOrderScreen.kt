package com.example.tassty.screen.detailorder

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.OrderStatus
import com.example.tassty.R
import com.example.tassty.component.DebitSmallPaymentCard
import com.example.tassty.component.DeliveryDetailCard
import com.example.tassty.component.DeliveryDriverCard
import com.example.tassty.component.DeliveryLocationCard
import com.example.tassty.component.Divider
import com.example.tassty.component.Divider32
import com.example.tassty.component.ErrorScreen
import com.example.tassty.component.OrderStatusCard
import com.example.tassty.component.OrderStepProgress
import com.example.tassty.component.OrderSummaryCard
import com.example.tassty.dummyDetail
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun DetailOrderScreen(
    viewModel: DetailOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resource = uiState.detail

    when {
        resource.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
        }
        resource.errorMessage != null -> {
            ErrorScreen()
        }
        else -> {
            resource.data?.let { detail ->
                DetailOrderContent(detail = detail)
            }
        }
    }
}

@Composable
fun DetailOrderContent(
    detail: DetailOrderUiModel
) {
    val restaurantLoc = LatLng(-8.705, 116.275)
    val userLoc = LatLng(detail.userAddress.latitude, detail.userAddress.longitude)
    var driverPos by remember { mutableStateOf(restaurantLoc) }

    val animatedDriverLatLng by animateValueAsState(
        targetValue = driverPos,
        typeConverter = LatLngTypeConverter,
        animationSpec = tween (durationMillis = 2000, easing = LinearEasing),
        label = "DriverPosAnim"
    )

    LaunchedEffect(detail.status) {
        if (detail.status == OrderStatus.ON_DELIVERY) {
            while (driverPos.latitude > userLoc.latitude) {
                delay(2000)
                driverPos = LatLng(driverPos.latitude - 0.0005, driverPos.longitude + 0.0005)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(restaurantLoc, 15f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            Marker(state = MarkerState(position = restaurantLoc), title = "Indah Café")
            Marker(state = MarkerState(position = userLoc), title = "You")

            if (detail.status == OrderStatus.ON_DELIVERY) {
                Marker(
                    state = MarkerState(position = animatedDriverLatLng),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                    zIndex = 1f
                )
                Polyline(points = listOf(restaurantLoc, animatedDriverLatLng), color = Blue500, width = 10f)
            }

            Polyline(points = listOf(restaurantLoc, userLoc), color = Neutral30, width = 10f)
        }

        DraggableOrderDetail(
            detail = detail,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun DraggableOrderDetail(
    detail: DetailOrderUiModel,
    modifier: Modifier = Modifier,
    minHeight: Dp = 340.dp,
    maxHeight: Dp = 600.dp
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
            .background(Neutral10)
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
                    color = Neutral100
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.shopping_bag),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Orange500
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = detail.orderNumber,
                        style = LocalCustomTypography.current.bodySmallRegular,
                        color = Neutral70
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "•",
                        style = LocalCustomTypography.current.bodySmallRegular,
                        color = Neutral40
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Blue500
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Today, 09:35 AM",
                        style = LocalCustomTypography.current.bodySmallRegular,
                        color = Neutral70
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
                OrderDetailExpandedContent(detail ,expandProgress)
            }
        }
    }
}

@Composable
fun OrderDetailExpandedContent(
    detail: DetailOrderUiModel,
    expandProgress: Float
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(expandProgress)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(32.dp))
        Column (Modifier.padding(horizontal = 24.dp)) {
            DeliveryDriverCard(
                driver = detail.driver
            )
        }

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
            Spacer(Modifier.height(24.dp))
        }

    }
}

val LatLngTypeConverter = TwoWayConverter<LatLng, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.latitude.toFloat(), it.longitude.toFloat()) },
    convertFromVector = { LatLng(it.v1.toDouble(), it.v2.toDouble()) }
)

@Preview(showBackground = true)
@Composable
fun OrderTrackingScreen() {
    DetailOrderContent(
        detail = dummyDetail,
    )
}