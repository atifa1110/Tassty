package com.example.tassty.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity

@Composable
fun WormIndicator(
    pagerState: PagerState,
    totalDots: Int,
    modifier: Modifier = Modifier,
    baseSize: Dp = 6.dp,
    selectedWidth: Dp = 24.dp, // panjang kalau aktif
    spacing: Dp = 8.dp,
    selectedColor: Color = Color(0xFFEE8E1F),
    unselectedColor: Color = Color(0xFFFADCBA)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = pagerState.currentPage == index

            // animasi lebar ketika dipilih
            val width by animateDpAsState(
                targetValue = selectedWidth,
                label = "widthAnim"
            )
            val color by animateColorAsState(
                targetValue = if (isSelected) selectedColor else unselectedColor,
                label = "colorAnim"
            )

            Box(
                modifier = Modifier
                    .height(baseSize)   // tinggi tetap
                    .width(width)
                    .clip(CircleShape)  // biar ujungnya tetap rounded
                    .background(color)
            )
        }
    }
}


@Composable
fun DashedDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 4.dp, // Panjang setiap strip
    gapLength: Dp = 4.dp   // Panjang celah
) {
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val dashPx = with(density) { floatArrayOf(dashLength.toPx(), gapLength.toPx()) }

    Spacer(
        modifier
            .fillMaxWidth()
            .height(strokeWidth) // Tinggi = tebal garis
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidthPx,
                    pathEffect = PathEffect.dashPathEffect(dashPx, 0f)
                )
            }
    )
}