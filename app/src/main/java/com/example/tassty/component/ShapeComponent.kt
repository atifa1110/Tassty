package com.example.tassty.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ChatBubbleShape(private val isMine: Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val cornerRadius = with(density) { 16.dp.toPx() }
            val tipSize = with(density) { 8.dp.toPx() }

            if (isMine) {
                // --- KANAN (Ujung di Atas Kanan) ---
                moveTo(cornerRadius, 0f)
                lineTo(size.width - cornerRadius, 0f) // Garis ke arah pojok kanan atas
                lineTo(size.width + tipSize, 0f)      // Ujung lancip keluar ke kanan
                lineTo(size.width, tipSize)           // Turun kembali ke body bubble

                lineTo(size.width, size.height - cornerRadius)
                quadraticTo(size.width, size.height, size.width - cornerRadius, size.height)
                lineTo(cornerRadius, size.height)
                quadraticTo(0f, size.height, 0f, size.height - cornerRadius)
                lineTo(0f, cornerRadius)
                quadraticTo(0f, 0f, cornerRadius, 0f)
            } else {
                // --- KIRI (Ujung di Atas Kiri) ---
                moveTo(0f, tipSize)                   // Titik setelah lancip
                lineTo(-tipSize, 0f)                  // Ujung lancip keluar ke kiri atas
                lineTo(cornerRadius, 0f)              // Kembali ke garis atas

                lineTo(size.width - cornerRadius, 0f)
                quadraticTo(size.width, 0f, size.width, cornerRadius)
                lineTo(size.width, size.height - cornerRadius)
                quadraticTo(size.width, size.height, size.width - cornerRadius, size.height)
                lineTo(cornerRadius, size.height)
                quadraticTo(0f, size.height, 0f, size.height - cornerRadius)
                lineTo(0f, cornerRadius)
                quadraticTo(0f, tipSize, 0f, tipSize) // Menutup ke arah lancip kiri
            }
            close()
        }
        return Outline.Generic(path)
    }
}