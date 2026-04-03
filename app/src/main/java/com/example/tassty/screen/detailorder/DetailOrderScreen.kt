package com.example.tassty.screen.detailorder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.OrderItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.tassty.R
import com.example.tassty.component.ButtonComponent
import com.example.tassty.component.CircleImageIcon
import com.example.tassty.component.DebitSmallPaymentCard
import com.example.tassty.component.DeliveryDriverCard
import com.example.tassty.component.DeliveryLocationCard
import com.example.tassty.component.Divider32
import com.example.tassty.component.HeaderListBlackTitle
import com.example.tassty.component.OrderDetailAppBar
import com.example.tassty.component.OrderMenuListCard
import com.example.tassty.component.OrderStatusCard
import com.example.tassty.component.OrderSummaryCard
import com.example.tassty.navigation.RatingNavArg
import com.example.tassty.screen.rating.HeaderRating
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.util.dummyDetail
import com.example.tassty.ui.theme.TasstyTheme

@Composable
fun DetailOrderScreen(
    onNavigateBack:()-> Unit,
    onNavigateToRating: (RatingNavArg) -> Unit,
    viewModel: DetailOrderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { event->
            when(event){
                is DetailOrderEvent.NavigateToRating -> {
                    onNavigateToRating(event.data)
                }
                is DetailOrderEvent.ShowMessage -> {
                    Toast.makeText(context,event.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val detail = uiState.detail.data

    if(detail != null){
        DetailOrderContent(
            detail = detail,
            onNavigateBack = onNavigateBack,
            onRatingRestClick = viewModel::onRatingRestClick,
            onRatingMenuClick = viewModel::onRatingMenuClick
        )
    }
}

@Composable
fun DetailOrderContent(
    detail: DetailOrderUiModel,
    onNavigateBack:()-> Unit,
    onRatingRestClick:() -> Unit,
    onRatingMenuClick: (OrderItemUiModel) -> Unit
) {
    Scaffold(
        containerColor = LocalCustomColors.current.background,
        topBar = {
            OrderDetailAppBar(
                isReviewId = detail.restaurantReviewId.isNotEmpty(),
                onBackClick = onNavigateBack,
                onEditClick = onRatingRestClick
            )
        },
        bottomBar = {
            Row (modifier = Modifier.fillMaxWidth()
                .background(LocalCustomColors.current.modalBackgroundFrame)
                .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                CircleImageIcon(
                    boxColor = LocalCustomColors.current.topBarBackgroundColor,
                    icon = Icons.Default.Download,
                    iconColor = LocalCustomColors.current.iconFocused,
                    contentDescription = "download button",
                    iconSize = 34.dp,
                    modifier = Modifier.size(60.dp)
                )
                ButtonComponent(
                    modifier = Modifier.weight(1f),
                    labelResId = R.string.reorder,
                    onClick = {}
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item(key = "header"){
                HeaderRating(
                    title = "Rating Food",
                    orderNumber = detail.orderNumber,
                    createdAt = detail.createdAt
                )
                Divider32()
            }

            item(key = "delivery_status"){
                OrderStatusCard(currentStatus = OrderStatus.COMPLETED)
                Divider32()
            }

            item(key = "driver_section"){
                DeliveryDriverCard(
                    driver = detail.driver,
                    onMessageClick = {},
                    isProcess = false
                )
                Divider32()
            }

            item(key = "location_section"){
                DeliveryLocationCard(
                    restaurant = detail.restaurant,
                    userAddress = detail.userAddress
                )
                Divider32()
            }

            item(key = "menu_header"){
                HeaderListBlackTitle(
                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
                    title = "Delivery detail"
                )
            }

            items(
                items = detail.orderItems,
                key = { it.id }
            ) { order ->
                Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    OrderMenuListCard(
                        item = order,
                        onClick = { onRatingMenuClick(order) }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            item(key = "summary_section"){
                Column(Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OrderSummaryCard(
                        isPercentageDiscount = false,
                        totalPrice = detail.totalPrice,
                        deliveryFee = detail.deliveryFee,
                        voucherDiscount = detail.discount,
                        totalOrder = detail.finalAmount
                    )
                    DebitSmallPaymentCard(
                        card = detail.cardPayment
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true, name = "Light Mode")
//@Composable
//fun DetailOrderLightPreview() {
//    TasstyTheme{
//        DetailOrderContent(
//            detail = dummyDetail,
//            onNavigateBack = {},
//            onRatingMenuClick = {},
//            onRatingRestClick = {}
//        )
//    }
//}

//@Preview(showBackground = true, name = "Dark Mode")
//@Composable
//fun DetailOrderDarkPreview() {
//    TasstyTheme(darkTheme = true) {
//        DetailOrderContent(
//            detail = dummyDetail,
//            onNavigateBack = {},
//            onRatingMenuClick = {},
//            onRatingRestClick = {}
//        )
//    }
//}