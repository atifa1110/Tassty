package com.example.tassty.model

import androidx.compose.ui.graphics.Color
import com.example.tassty.R
import com.example.tassty.ui.theme.Blue200
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green200
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink500

data class CardColorOption(
    val id: String,
    val color: Color,
    val selectedColor: Color
)

val colorList = listOf(
    CardColorOption("pink", Pink200, Pink500),
    CardColorOption("orange", Orange200, Orange500),
    CardColorOption("green", Green200, Green500),
    CardColorOption("purple", Blue200, Blue500)
)
val patterns = listOf(
    R.drawable.background_design,
    R.drawable.background_design_2,
    R.drawable.background_design_3
)