package com.example.tassty.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


// ==== ORANGE (Primary) ====
val Orange900 = Color(0xFF45300D)
val Orange800 = Color(0xFF734E1F)
val Orange700 = Color(0xFFA16D35)
val Orange600 = Color(0xFFD78C4B)
val Orange500 = Color(0xFFF07C2A)
val Orange400 = Color(0xFFFAAB56)
val Orange300 = Color(0xFFFBB974)
val Orange200 = Color(0xFFFBCDA0)
val Orange100 = Color(0xFFFADCBA)
val Orange50  = Color(0xFFFDEEDB)

// ==== GREEN (Success) ====
val Green900 = Color(0xFF243D30)
val Green800 = Color(0xFF175546)
val Green700 = Color(0xFF187260)
val Green600 = Color(0xFF1B876B)
val Green500 = Color(0xFF18A879)
val Green400 = Color(0xFF31C4A0)
val Green300 = Color(0xFF70D2BB)
val Green200 = Color(0xFF9EDCCD)
val Green100 = Color(0xFFCFE9DF)
val Green50  = Color(0xFFF0F9F5)

// ==== PINK (Danger) ====
val Pink900 = Color(0xFF61152E)
val Pink800 = Color(0xFF851E40)
val Pink700 = Color(0xFFA32D57)
val Pink600 = Color(0xFFCA2D77)
val Pink500 = Color(0xFFE74197)
val Pink400 = Color(0xFFF15FAD)
val Pink300 = Color(0xFFF58ABF)
val Pink200 = Color(0xFFF7B7D2)
val Pink100 = Color(0xFFF9D3E0)
val Pink50  = Color(0xFFFDECF3)

// ==== BLUE (Info) ====
val Blue900 = Color(0xFF232D68)
val Blue800 = Color(0xFF3F2D9B)
val Blue700 = Color(0xFF513AD0)
val Blue600 = Color(0xFF6446E2)
val Blue500 = Color(0xFF7251FB)
val Blue400 = Color(0xFFA18EF7)
val Blue300 = Color(0xFFADA8FA)
val Blue200 = Color(0xFFC8C2FC)
val Blue100 = Color(0xFFDCD5FD)
val Blue50  = Color(0xFFF3F0FF)

// ==== NEUTRAL (Gray Scale) ====
val Neutral100 = Color(0xFF1F1F1F)
val Neutral80  = Color(0xFF444444)
val Neutral70  = Color(0xFF656565)
val Neutral60  = Color(0xFF8F8F8F)
val Neutral40  = Color(0xFFD2D2D2)
val Neutral30  = Color(0xFFE8E8E8)
val Neutral20  = Color(0xFFF4F4F7)
val Neutral10  = Color(0xFFFFFFFF)
val modal = Color(0xFF1C202B).copy(0.6f)
// Dark Mode Colors
val DarkBackground = Color(0xFF191B1D)
val DarkBackgroundSlider = Color(0xFF191B1D).copy(0.84f)
val DarkBackgroundPopup = Color(0xFF191B1D)
val DarkBackgroundFrame = Color(0xFF3A4153).copy(0.6f)

// Card
val CardOutlineDefault = Color(0xFF323639)
val CardBackgroundDefault = Color(0xFF2E2E33)
val CardBackgroundDisable = Color(0xFF4A5156)
val CardBackgroundDisableSlider = Color(0xFF555E65)

// Chips
val ChipsActive = Color(0xFF422604)
val DarkOrangeBackground = Color(0xFF422604)
val ErrorBackground = Color(0xFF410E27)

val DarkPink = Color(0xFF410E27)
val DarkOrange = Color(0xFF422604)
val DarkBlue = Color(0xFF201354)
val DArkGreen =Color(0xFF1C392E)

data class CustomColors(
    val background: Color,
    val searchBackground: Color,
    val sliderBackground: Color,
    val modalBackground: Color,
    val modalBackgroundFrame: Color,
    val frameBackground: Color,
    val buttonDisableBackground: Color,
    val buttonTextDisableBackground: Color,
    val cardBackground: Color,
    val cardBackground2: Color,
    val divider: Color,
    val dividerCard: Color,
    val topBarBackgroundColor : Color,
    val topBarBorder: Color,
    val selectedOrangeBackground: Color,
    val selectedOrangeStroke: Color,
    val headerText: Color,
    val text: Color,
    val iconFocused: Color,
    val iconDisable: Color,
    val border: Color,
    val borderUnfocused: Color,
    val errorBorder: Color,
    val errorBackground: Color,
    val pink: Color,
    val orange: Color,
    val green: Color,
    val blue: Color,
    val processStatus: Color,
    val completedStatus: Color,
    val cancelStatus: Color,
    val switchThumb: Color,
    val switchTrack: Color
)