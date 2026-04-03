package com.example.tassty.model

import androidx.compose.ui.graphics.Color
import com.example.tassty.R
import com.example.tassty.ui.theme.Blue100
import com.example.tassty.ui.theme.Blue200
import com.example.tassty.ui.theme.Blue50
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green100
import com.example.tassty.ui.theme.Green200
import com.example.tassty.ui.theme.Green50
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Orange100
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange300
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink100
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500

data class CardColorOption(
    val id: String,
    val backgroundColor : Color,
    val borderColor: Color,
    val imageBackground: Color
)

data class PatternImage(
    val id: String,
    val imageRes: Int
)


val colorList = listOf(
    CardColorOption("pink", Pink200, Pink500, Pink50),
    CardColorOption("orange", Orange200, Orange500,Orange50),
    CardColorOption("green", Green200, Green500, Green50),
    CardColorOption("purple", Blue200, Blue500, Blue50)
)
val patterns = listOf(
    PatternImage("pattern_1",R.drawable.card_pattern_1),
    PatternImage("pattern_2",R.drawable.card_pattern_2),
    PatternImage("pattern_3",R.drawable.card_pattern_3)
)

fun String.toCardColor(): CardColorOption {
    return colorList.find { it.id == this } ?: colorList.first()
}

fun String.toPatternRes(): Int {
    return patterns.find { it.id == this }?.imageRes ?: R.drawable.card_pattern_1
}


fun String.toLogoRes(): Int {
    return when (this.lowercase()) {
        "visa" -> R.drawable.visa
        "mastercard" -> R.drawable.mastercard
        else -> R.drawable.credit_card
    }
}

