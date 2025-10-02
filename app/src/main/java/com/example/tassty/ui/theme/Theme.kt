package com.example.tassty.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

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

// Ini adalah CompositionLocal yang kita buat sebelumnya
val LocalCustomTypography = staticCompositionLocalOf { CustomTypography() }

@Composable
fun TasstyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Gunakan CompositionLocalProvider untuk menyediakan Typography kustom
    CompositionLocalProvider(LocalCustomTypography provides CustomTypography()) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
}