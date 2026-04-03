package com.example.tassty.screen.orderprocess

import android.widget.Toast
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import com.example.tassty.calculatePathTraveled
import com.example.tassty.component.CompletedContent
import com.example.tassty.component.CustomBottomSheet
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
import com.example.tassty.getDirections
import com.example.tassty.loadCircularBitmapFromUrl
import com.example.tassty.screen.rating.HeaderIconText
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.dummyDetail
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@Composable
fun OrderProcessScreen(
    onNavigateToMessage : (String) -> Unit,
    viewModel: OrderProcessViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resource = uiState.detail

    LaunchedEffect(true) {
        viewModel.events.collect { event->
            when(event){
                is OrderProcessEvent.NavigateToMessage -> {
                    onNavigateToMessage(event.channelId)
                }
                is OrderProcessEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
                OrderProcessContent(
                    detail = detail,
                    onMessageClick = viewModel::onChatClicked
                )
            }
        }
    }

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
    detail: DetailOrderUiModel,
    onMessageClick:() -> Unit
) {
    val context = LocalContext.current

    // 1. Lokasi (Ganti koordinat resto sesuai data aslimu)
    val restaurantLoc = LatLng(-6.385, 106.830)
    val userLoc = LatLng(detail.userAddress.latitude, detail.userAddress.longitude)

    // 2. States untuk Icons & Rute
    var restaurantMarkerIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var userMarkerIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var roadPoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    // 3. State Driver (Posisi Driver & Animasinya)
    var driverPos by remember { mutableStateOf(restaurantLoc) }
    val animatedDriverLatLng by animateValueAsState(
        targetValue = driverPos,
        typeConverter = LatLngTypeConverter,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "DriverPosAnim"
    )

    LaunchedEffect(Unit) {
        // Load Icons
        restaurantMarkerIcon = loadCircularBitmapFromUrl(context, detail.restaurant.imageUrl, 48.dp)
        userMarkerIcon = loadCircularBitmapFromUrl(context, "https://link-foto-user.jpg", 40.dp)

        val path = getDirections(origin = restaurantLoc, dest = userLoc)
        println("DEBUG_MAP: Jumlah titik jalan = ${path.size}")
        roadPoints = path
    }

    LaunchedEffect(detail.status, roadPoints) {
        if (detail.status == OrderStatus.ON_DELIVERY && roadPoints.isNotEmpty()) {
            for (point in roadPoints) {
                driverPos = point
                delay(1000)
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(restaurantLoc, 14f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false, tiltGesturesEnabled = false)
        ) {
            restaurantMarkerIcon?.let { icon ->
                Marker(
                    state = MarkerState(position = restaurantLoc),
                    icon = icon,
                    anchor = Offset(0.5f, 0.5f),
                    title = detail.restaurant.name
                )
            }

            userMarkerIcon?.let { icon ->
                Marker(
                    state = MarkerState(position = userLoc),
                    icon = icon,
                    anchor = Offset(0.5f, 0.5f),
                    title = "You"
                )
            }

            if (roadPoints.isNotEmpty()) {
                Polyline(
                    points = roadPoints,
                    color = Neutral70,
                    width = 12f,
                    jointType = JointType.ROUND,
                    startCap = RoundCap(),
                    endCap = RoundCap()
                )
            }

            if (detail.status == OrderStatus.ON_DELIVERY && roadPoints.isNotEmpty()) {
                val pathTraveled = calculatePathTraveled(roadPoints, driverPos)

                Polyline(
                    points = pathTraveled,
                    color = Blue500,
                    width = 12f,
                    zIndex = 1f,
                    jointType = JointType.ROUND,
                    startCap = RoundCap(),
                    endCap = RoundCap()
                )

                Marker(
                    state = MarkerState(position = animatedDriverLatLng),
                    anchor = Offset(0.5f, 0.5f)
                )
            }
        }

        DraggableOrderProcess(
            detail = detail,
            modifier = Modifier.align(Alignment.BottomCenter),
            onMessageClick = onMessageClick
        )
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
    Column(modifier = Modifier
            .fillMaxWidth()
            .alpha(expandProgress)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(32.dp))
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
        Spacer(Modifier.height(32.dp))
    }
}

val LatLngTypeConverter = TwoWayConverter<LatLng, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.latitude.toFloat(), it.longitude.toFloat()) },
    convertFromVector = { LatLng(it.v1.toDouble(), it.v2.toDouble()) }
)


//@Preview(showBackground = true)
//@Composable
//fun OrderProcessLightPreview(){
//    TasstyTheme(darkTheme = true){
//        OrderProcessContent(
//            detail = dummyDetail
//        ) { }
//    }
//}