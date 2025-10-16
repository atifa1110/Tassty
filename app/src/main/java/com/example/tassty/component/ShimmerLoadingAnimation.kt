package com.example.tassty.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange500

fun Modifier.shimmerLoadingAnimation(
    widthOfShadowBrush: Int = 500,
    durationMillis: Int = 1000
): Modifier = composed {

    // 1. Ambil Ukuran Composable
    var size by remember { mutableStateOf(IntSize.Zero) }

    // 2. Transisi Animasi Berulang
    val transition = rememberInfiniteTransition(label = "ShimmerInfiniteTransition")

    // 3. Animasi Posisi X dari Gradien
    val xShimmer by transition.animateFloat(
        initialValue = -widthOfShadowBrush.toFloat(),
        targetValue = size.width.toFloat() + widthOfShadowBrush.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = FastOutSlowInEasing
            )
        ),
        label = "XShimmer"
    )

    // 4. Definisikan Warna dan Brush
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.9f), // Awal (terlihat)
        Color.LightGray.copy(alpha = 0.2f), // Tengah (transparan)
        Color.LightGray.copy(alpha = 0.9f), // Akhir (terlihat)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(xShimmer - widthOfShadowBrush.toFloat(), 0f),
        end = Offset(xShimmer, 0f)
    )

    // 5. Terapkan Modifier
    this
        .onGloballyPositioned {
            size = it.size // Simpan ukuran Composable
        }
        .background(brush) // Terapkan brush sebagai background
}

@Composable
fun ShimmerHorizontalTitleButtonSection(
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.4f).clip(RoundedCornerShape(20.dp))
                    .height(14.dp)
                    .shimmerLoadingAnimation()
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.4f).clip(RoundedCornerShape(20.dp))
                    .height(14.dp)
                    .shimmerLoadingAnimation()
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            content()
        }
    }
}


@Composable
fun ShimmerGridMenuListPlaceholder(
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(0.4f)
                .height(14.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerLoadingAnimation()
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 2
        ) {
            repeat(4) {
                ShimmerFoodLargeGridCard(modifier = Modifier.weight(1f, fill = true))
            }
        }
    }
}