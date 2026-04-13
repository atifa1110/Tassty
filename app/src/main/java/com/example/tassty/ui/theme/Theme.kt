package com.example.tassty.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = CardBackgroundDefault,
    onBackground = Neutral10,
    onSurface = Neutral10,
    onSurfaceVariant = Neutral40,
    primary = ChipsActive,
    outline = CardOutlineDefault,
)

private val LightColorScheme = lightColorScheme(
    background = Neutral10,
    surface = Neutral10,
    onBackground = Neutral10,
    onSurface = Neutral10,
    onSurfaceVariant = Neutral70,
    primary = ChipsActive,
    outline = CardOutlineDefault,
)

private val DarkCustomColors = CustomColors(
    background = DarkBackground,
    sliderBackground = DarkBackgroundSlider,
    searchBackground = DarkBackground,
    modalBackground = DarkBackgroundFrame,
    modalBackgroundFrame = DarkBackgroundFrame,
    cardBackground = CardBackgroundDefault,
    cardBackground2 = CardBackgroundDefault,
    frameBackground = DarkBackgroundFrame,
    buttonDisableBackground = CardBackgroundDisable,
    buttonTextDisableBackground = Neutral40,
    divider = CardOutlineDefault,
    dividerCard = CardBackgroundDisable,
    topBarBackgroundColor = DarkBackgroundFrame,
    topBarBorder = CardBackgroundDisable,
    selectedOrangeStroke = Orange700,
    selectedOrangeBackground = DarkOrangeBackground,
    headerText = Neutral10,
    text = Neutral40,
    iconFocused = Neutral10,
    iconDisable = Neutral40,
    border = CardOutlineDefault,
    borderUnfocused = CardBackgroundDisable,
    errorBorder = Pink400,
    errorBackground = ErrorBackground,
    pink = DarkPink,
    orange = DarkOrange,
    green = DArkGreen,
    blue = DarkBlue,
    processStatus = Blue300,
    completedStatus = Green300,
    cancelStatus = Pink300,
    switchThumb = Neutral40,
    switchTrack = Neutral70
)

private val LightCustomColors = CustomColors(
    background = Neutral10,
    sliderBackground = Neutral10.copy(0.9f),
    searchBackground = Neutral10.copy(0.20f),
    modalBackground = modal,
    modalBackgroundFrame = Neutral10.copy(0.9f),
    cardBackground = Neutral20,
    cardBackground2 = Neutral10,
    frameBackground = Neutral10.copy(0.9f),
    buttonDisableBackground = Neutral40,
    buttonTextDisableBackground = Neutral100,
    divider = Neutral30,
    dividerCard = Neutral40,
    topBarBackgroundColor = Neutral20,
    topBarBorder = Color.Transparent,
    selectedOrangeStroke = Orange200,
    selectedOrangeBackground = Orange50,
    headerText = Neutral100,
    text = Neutral70,
    iconFocused = Neutral100,
    iconDisable = Neutral70,
    border = Neutral30,
    borderUnfocused = Neutral30,
    errorBorder = Pink600,
    errorBackground = Pink50,
    pink = Pink50,
    orange = Orange50,
    green = Green50,
    blue = Blue50,
    processStatus = Blue600,
    completedStatus = Green600,
    cancelStatus = Pink600,
    switchThumb = Neutral80,
    switchTrack = Neutral40
)


val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }
val LocalCustomTypography = staticCompositionLocalOf { CustomTypography() }

@Composable
fun TasstyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalCustomTypography provides CustomTypography(),
        LocalCustomColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}