package com.example.tassty.component

import androidx.compose.ui.graphics.Color
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.model.ChipType
import com.example.tassty.model.FilterKey
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70

data class ChipStyle(
    val backgroundColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val iconColor: Color
)

fun FilterKey.toChipStyle(isSelected: Boolean): ChipStyle {
    if (!isSelected) {
        return ChipStyle(
            backgroundColor = Neutral20,
            borderColor = Color.Transparent,
            textColor = Neutral70,
            iconColor = Neutral70
        )
    }

    return when (this) {
        FilterKey.RestoRating -> ChipStyle(
            backgroundColor = Color(0xFFFFF59D), // kuning muda
            borderColor = Color(0xFFFFC107),
            textColor = Color(0xFF795548),
            iconColor = Color(0xFF795548)
        )

        FilterKey.Discount -> ChipStyle(
            backgroundColor = Blue500,
            borderColor = Color.Transparent,
            textColor = Color.White,
            iconColor = Color.White
        )

        FilterKey.PriceRange -> ChipStyle(
            backgroundColor = Color(0xFFE3F2FD),
            borderColor = Color(0xFF64B5F6),
            textColor = Color(0xFF0D47A1),
            iconColor = Color(0xFF0D47A1)
        )

        FilterKey.Mode -> ChipStyle(
            backgroundColor = Color(0xFFE8F5E9),
            borderColor = Color(0xFF81C784),
            textColor = Color(0xFF2E7D32),
            iconColor = Color(0xFF2E7D32)
        )

        FilterKey.Cuisine -> ChipStyle(
            backgroundColor = Color(0xFFFFEBEE),
            borderColor = Color(0xFFEF5350),
            textColor = Color(0xFFE64A19),
            iconColor = Color(0xFFE64A19)
        )
    }
}

