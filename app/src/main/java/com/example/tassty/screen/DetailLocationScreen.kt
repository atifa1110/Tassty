package com.example.tassty.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.component.BackTopAppBar
import com.example.tassty.component.ItemImage
import com.example.tassty.component.RestaurantShortInfoCard
import com.example.tassty.component.TopBarButton
import com.example.tassty.model.RestaurantStatus
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun DetailLocationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapArea(modifier = Modifier.fillMaxSize())

        BackTopAppBar(onBackClick = {})

        DraggableRestaurantBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun DraggableRestaurantBar(
    modifier: Modifier = Modifier,
    bottomSpacing: Dp = 32.dp,
    minHeight: Dp = 70.dp,
    maxHeight: Dp = 400.dp
) {
    var offsetY by remember { mutableStateOf(-250f) }

    // Konversi offsetY jadi tinggi dinamis (dibalik biar drag ke atas = tinggi tambah)
    val dynamicHeight by animateDpAsState(
        targetValue = (minHeight - offsetY.dp).coerceIn(minHeight, maxHeight),
        label = "heightAnim"
    )

    // === Animasi padding bawah & horizontal ===
    val dynamicBottomPadding by animateDpAsState(
        targetValue = if (offsetY >= 0f) bottomSpacing else 0.dp,
        label = "bottomPaddingAnim"
    )
    val dynamicHorizontalPadding by animateDpAsState(
        targetValue = if (offsetY >= 0f) 16.dp else 0.dp,
        label = "horizontalPaddingAnim"
    )

    val dynamicRestaurantPadding by animateDpAsState(
        targetValue = if (dynamicHeight > minHeight * 2)
            24.dp else 12.dp,
        label = "horizontalPaddingAnim"
    )

    // === Animasi sudut dinamis ===
    val topCorner by animateDpAsState(
        targetValue = if (dynamicHeight > minHeight * 2) 24.dp else 99.dp,
        label = "topCornerAnim"
    )

    val bottomCorner by animateDpAsState(
        targetValue = if (dynamicHeight > minHeight * 2) 0.dp else 99.dp,
        label = "bottomCornerAnim"
    )

    Box(
        modifier = modifier
            .padding(
                bottom = dynamicBottomPadding,
                start = dynamicHorizontalPadding,
                end = dynamicHorizontalPadding
            )
            .fillMaxWidth()
            .height(dynamicHeight) // ← tinggi ikut drag
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(
                    color = Neutral20,
                    shape = RoundedCornerShape(
                        topStart = topCorner,
                        topEnd = topCorner,
                        bottomStart = bottomCorner,
                        bottomEnd = bottomCorner
                    )
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(dynamicRestaurantPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row (horizontalArrangement = Arrangement.spacedBy(12.dp)){
                    ItemImage(
                        imageUrl = "https://tse3.mm.bing.net/th/id/OIP.MO6T-LKR9oi03dJSe9DMGgHaE8?rs=1&pid=ImgDetMain&o=7&rm=3",
                        name = "restaurant location image",
                        status = RestaurantStatus.OPEN,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(if (dynamicHeight > 100.dp) RoundedCornerShape(8.dp) else CircleShape)
                    )
                    Column {
                        Text(
                            text = "Indah Café",
                            style = if (dynamicHeight > 100.dp) LocalCustomTypography.current.h3Bold else LocalCustomTypography.current.h5Bold,
                            color = Neutral100
                        )
                        Text(
                            text = "Gerunung Lombok Tengah, Praya",
                            style = LocalCustomTypography.current.bodySmallMedium,
                            color = Neutral70
                        )
                    }
                }
                if(dynamicHeight > 100.dp) {
                    TopBarButton(
                        icon = R.drawable.phone,
                        boxColor = Orange500, iconColor = Neutral10
                    ) { }
                }
            }

            HorizontalDivider(Modifier.padding(bottom = 32.dp))

            Column (
                Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)){
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        tint = Pink500,
                        contentDescription = "restaurant address"
                    )
                    
                    Text(
                        text = "Jl. Raya Praya, Mantang Depan Gerunung, \nLombok Tengah",
                        style = LocalCustomTypography.current.bodyMediumRegular,
                        color = Neutral70
                    )
                }

                RestaurantShortInfoCard(onReviewsClick = {}, onScheduleClick = {})
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-5).dp)
                .size(width = 40.dp, height = 6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Gray.copy(alpha = 0.6f))
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        offsetY += delta // update offset
                    }
                )
        )
    }
}

@Preview
@Composable
fun PreviewDetailLocationScreen() {
    DetailLocationScreen()
}