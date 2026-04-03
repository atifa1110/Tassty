package com.example.tassty.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.DriverUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.OrderItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.UserAddressUiModel
import com.example.tassty.R
import com.example.tassty.StatusUIConfig
import com.example.tassty.getUIConfig
import com.example.tassty.util.orderList
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Blue600
import com.example.tassty.ui.theme.Green300
import com.example.tassty.ui.theme.Green50
import com.example.tassty.ui.theme.Green600
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Orange600
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink600
import com.example.tassty.ui.theme.TasstyTheme
import com.example.tassty.util.dummyDetail
import com.example.tassty.util.listOrder

@Composable
fun OrderListCard(
    order: OrderUiModel,
    onCardClick:(OrderUiModel) -> Unit
){
    val (text, color) = when (order.status) {
        OrderStatus.PLACED ,OrderStatus.PENDING,OrderStatus.PREPARING,OrderStatus.ON_DELIVERY -> "Processed" to LocalCustomColors.current.processStatus
        OrderStatus.COMPLETED -> "Completed" to LocalCustomColors.current.completedStatus
        OrderStatus.CANCELLED -> "Cancelled" to LocalCustomColors.current.cancelStatus
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = {onCardClick(order)}),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(10.dp)
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommonImage(
                modifier = Modifier
                    .width(68.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp)),
                imageUrl = order.restaurantImage,
                name = order.orderNumber
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        color = LocalCustomColors.current.headerText,
                        style = LocalCustomTypography.current.h5Bold,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append(order.restaurantName)
                            }
                            withStyle(style = SpanStyle(color =LocalCustomColors.current.text)) {
                                append(" - ")
                            }
                            withStyle(style = SpanStyle()) {
                                append(order.queueNumber)
                            }
                        }
                    )
                    FoodPriceText(
                        price = order.finalAmount,
                        color = Orange500
                    )
                }

                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = "Time",
                        tint = Blue600,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = order.displayTime,
                        style = LocalCustomTypography.current.bodyXtraSmallMedium,
                        color = LocalCustomColors.current.text
                    )
                    Text(
                        text = "•",
                        style = LocalCustomTypography.current.bodyXtraSmallMedium,
                        color = Neutral70.copy(0.4f)
                    )
                    Text(
                        text = text,
                        style = LocalCustomTypography.current.bodyXtraSmallSemiBold,
                        color = color
                    )
                }

                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reorder",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Orange500
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.NorthEast,
                        contentDescription = "Reorder Icon",
                        tint = Orange500,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: OrderUiModel,
    onCardClick: (OrderUiModel) -> Unit
) {
    val (text, color) = when (order.status) {
        OrderStatus.PLACED ,OrderStatus.PENDING,OrderStatus.PREPARING,OrderStatus.ON_DELIVERY -> "Processed" to LocalCustomColors.current.processStatus
        OrderStatus.COMPLETED -> "Completed" to LocalCustomColors.current.completedStatus
        OrderStatus.CANCELLED -> "Cancelled" to LocalCustomColors.current.cancelStatus
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onCardClick(order) }),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.selectedOrangeBackground),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            CommonImage(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp)),
                imageUrl = order.restaurantImage,
                name = order.orderNumber
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        style = LocalCustomTypography.current.h6Bold,
                        color = LocalCustomColors.current.headerText,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append(order.restaurantName)
                            }
                            withStyle(style = SpanStyle()) {
                                append(" - ")
                            }
                            withStyle(style = SpanStyle()) {
                                val qNum = if (order.queueNumber.startsWith("#")) order.queueNumber else "#${order.queueNumber}"
                                append(qNum)
                            }
                        },
                        minLines = 1,
                    )

                    FoodPriceText(
                        price = order.finalAmount,
                        color = Orange500
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = null,
                        tint = Blue600,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = order.displayTime,
                        style = LocalCustomTypography.current.bodyXtraSmallMedium,
                        color = LocalCustomColors.current.text
                    )
                    Text(
                        text = "•",
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = LocalCustomColors.current.text
                    )
                    Text(
                        text = text,
                        style = LocalCustomTypography.current.bodyXtraSmallBold,
                        color = color
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onCardClick(order) }
                ) {
                    Text(
                        text = "See order",
                        style = LocalCustomTypography.current.bodySmallMedium,
                        color = Orange500
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.NorthEast,
                        contentDescription = null,
                        tint = Orange500,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderMenuListCard(
    item : OrderItemUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardBackground)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CommonImage(
                imageUrl = item.imageUrl,
                name = item.menuName,
                modifier = Modifier.size(80.dp)
                    .clip(CircleShape)
            )

            FoodOrderListCardContent(item = item, onClick = onClick)
        }
    }
}

@Composable
fun DeliveryDriverCard(
    driver: DriverUiModel,
    onMessageClick : () -> Unit,
    isProcess: Boolean = true
) {
    Column (Modifier.padding(horizontal = 24.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.background),
            border = BorderStroke(1.dp, Neutral40)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(44.dp)
                ) {
                    CommonImage(
                        imageUrl = driver.profileImage,
                        name = driver.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = (-2).dp, y = (0).dp)
                            .clip(CircleShape)
                            .background(Blue500),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_delivery),
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            colorFilter = ColorFilter.tint(Neutral10)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = driver.name,
                        style = LocalCustomTypography.current.h6Bold,
                        color = LocalCustomColors.current.headerText
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = LocalCustomTypography.current.bodyXtraSmallRegular.toSpanStyle()
                                ) {
                                    append("ID ")
                                }
                                withStyle(
                                    style = LocalCustomTypography.current.h8Bold.toSpanStyle()
                                ) {
                                    append("23455")
                                }
                            },
                            color = LocalCustomColors.current.text,
                            style = LocalCustomTypography.current.bodyXtraSmallRegular
                        )

                        Text(
                            text = " • ",
                            style = LocalCustomTypography.current.h8Bold,
                            color = LocalCustomColors.current.text
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.star),
                                contentDescription = "",
                                modifier = Modifier.size(12.dp),
                                tint = Orange500
                            )

                            Text(
                                text = driver.rating.toString(),
                                style = LocalCustomTypography.current.h8Bold,
                                color = LocalCustomColors.current.text,
                            )
                        }
                    }
                }
                if(isProcess) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        CircleImageIcon(
                            boxColor = Orange500,
                            icon = R.drawable.phone,
                            iconSize = 16.dp,
                            iconColor = Neutral10,
                            contentDescription = "top app bar icon",
                            modifier = Modifier.size(36.dp)
                        )

                        CircleImageIcon(
                            boxColor = Orange500,
                            icon = R.drawable.chat,
                            iconSize = 16.dp,
                            iconColor = Neutral10,
                            contentDescription = "top app bar icon",
                            modifier = Modifier.size(36.dp).clickable(
                                onClick = { onMessageClick() }
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryLocationCard(
    restaurant: RestaurantUiModel,
    userAddress: UserAddressUiModel
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Delivery details",
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
        )
        LocationCard(
            restaurant = restaurant,
            userAddress = userAddress
        )
    }
}


@Composable
fun LocationCard(
    restaurant: RestaurantUiModel,
    userAddress: UserAddressUiModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.background),
        border = BorderStroke(1.dp,Neutral40)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            LocationItem(
                label = "Restaurant location",
                address = restaurant.locationDetail.fullAddress,
                icon = Icons.Default.Store,
                iconBgColor = Orange100,
                iconColor = Orange600
            )

            DashedDivider(modifier = Modifier.padding(vertical = 16.dp))

            LocationItem(
                label = "Delivery location",
                address = userAddress.fullAddress,
                icon = Icons.Default.LocationOn,
                iconBgColor = Pink100,
                iconColor = Pink600
            )
        }
    }
}

@Composable
fun LocationItem(
    label: String,
    address: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconBgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = label,
                color = LocalCustomColors.current.text,
                style = LocalCustomTypography.current.bodyXtraSmallRegular
            )
            Text(
                text = address,
                color = LocalCustomColors.current.headerText,
                style = LocalCustomTypography.current.h6Bold
            )
        }
    }
}


@Composable
fun DeliveryDetailCard(
    order: List<OrderItemUiModel>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Delivery detail",
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.background),
            border = BorderStroke(1.dp, LocalCustomColors.current.border)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                order.forEach { item ->
                    OrderItem(
                        name = item.menuName,
                        qty = item.quantity,
                        note = item.notesSummary
                    )
                }
            }
        }
    }
}

@Composable
fun OrderItem(name: String, qty: String, note: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = LocalCustomTypography.current.bodySmallMedium,
                color = LocalCustomColors.current.headerText
            )
            Text(
                text = qty,
                style = LocalCustomTypography.current.h6Bold,
                color = LocalCustomColors.current.text
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = LocalCustomColors.current.cardBackground,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            NotesText(notes = note)
        }
    }
}

@Composable
fun OrderStatusCard(
    currentStatus: OrderStatus,
){
    val status = currentStatus.getUIConfig()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(status.containerColor).border(1.dp, status.strokeColor, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).background(status.iconColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = status.icon),
                        contentDescription = null,
                        tint = Neutral10,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = status.title,
                        style = LocalCustomTypography.current.h6Bold,
                        color = LocalCustomColors.current.headerText
                    )
                    Text(
                        text = status.description,
                        style = LocalCustomTypography.current.bodyXtraSmallRegular,
                        color = LocalCustomColors.current.text
                    )
                }
            }
        }
    }
}

@Composable
fun OrderStepProgress(
    currentStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val statuses = listOf(
        OrderStatus.PLACED,
        OrderStatus.PREPARING,
        OrderStatus.ON_DELIVERY,
        OrderStatus.COMPLETED
    )
    val currentIndex = statuses.indexOf(currentStatus)

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        statuses.forEachIndexed { index, status ->
            StatusCircle(
                isActive = index <= currentIndex,
                res = status.getUIConfig()
            )

            if (index < statuses.size - 1) {
                val isProcessing = index == currentIndex && status != OrderStatus.COMPLETED
                DashedLine(
                    modifier = Modifier.weight(1f),
                    isActive = index < currentIndex,
                    color = status.getUIConfig().iconColor,
                    isProcessing = isProcessing
                )
            }
        }
    }
}

@Composable
fun StatusCircle(isActive: Boolean, res : StatusUIConfig) {
    val bgColor = if (isActive) res.containerColor else Neutral30
    val iconColor = if (isActive) res.iconColor else Neutral60
    val borderColor = if (isActive) res.strokeColor else Color.Transparent

    Box(
        modifier = Modifier
            .size(44.dp)
            .border(1.dp, borderColor, CircleShape)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = res.icon),
            contentDescription = null,
            modifier = Modifier
                .size(22.dp),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}

@Composable
fun DashedLine(
    modifier: Modifier,
    isActive: Boolean,
    color: Color,
    isProcessing: Boolean = false
) {
    val lineColor = if (isActive || isProcessing) color else Neutral40

    val infiniteTransition = rememberInfiniteTransition(label = "dashAnim")

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.height(2.dp).padding(horizontal = 4.dp)) {
        drawLine(
            color = lineColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(10f, 10f),
                phase = if (isProcessing) -phase else 0f
            ),
            strokeWidth = 2.dp.toPx()
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun OrderCardPreview(){
//    TasstyTheme(darkTheme = true) {
//        Column (Modifier.fillMaxSize()){
//            OrderListCard(order = orderList[0]) { }
//            OrderCard(order = orderList[0]) { }
//            DeliveryDriverCard(driver = dummyDetail.driver, onMessageClick = {}, isProcess = false)
//            OrderMenuListCard(item = listOrder[1]) { }
//            DeliveryDetailCard(
//                listOrder
//            )
//        }
//    }
//}
//
