package com.example.tassty.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tassty.R

val ApfelGrotezkFontFamily = FontFamily(
    Font(R.font.apfel_grotezk_fett, FontWeight.ExtraBold),
    Font(R.font.apfel_grotezk_mittel, FontWeight.Bold),
    Font(R.font.apfel_grotezk_regular, FontWeight.Normal)
)

val PlusJakartaSansFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_extrabold, FontWeight.ExtraBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal), // Regular is often mapped to FontWeight.Normal
    Font(R.font.plus_jakarta_sans_light, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    // Headline/H1
    displayLarge = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 48.sp
    ),
)

data class CustomTypography(
    val h1ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 48.sp
    ),
    val h1Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp
    ),
    val h1Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 48.sp
    ),
    val h2ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    val h2Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    val h2Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    val h3ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),
    val h3Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),
    val h3Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 20.sp
    ),
    val h4ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    val h4Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    val h4Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    val h5ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    val h5Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    val h5Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    val h6ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp
    ),
    val h6Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    val h6Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val h7ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp
    ),
    val h7Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    val h7Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    val h8ExtraBold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 10.sp
    ),
    val h8Bold: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
    ),
    val h8Regular: TextStyle = TextStyle(
        fontFamily = ApfelGrotezkFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    // Body/Xtra Large
    val bodyXtraLargeExtraBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp
    ),
    val bodyXtraLargeBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    val bodyXtraLargeSemiBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    val bodyXtraLargeMedium: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    val bodyXtraLargeRegular: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    val bodyXtraLargeLight: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 18.sp
    ),
    // Body/Large
    val bodyLargeExtraBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    val bodyLargeBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    val bodyLargeSemiBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    val bodyLargeMedium: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    val bodyLargeRegular: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    val bodyLargeLight: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    // Body/Medium
    val bodyMediumExtraBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    val bodyMediumBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    val bodyMediumSemiBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    val bodyMediumMedium: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    val bodyMediumRegular: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    val bodyMediumLight: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 22.sp
    ),
    // Body/Small
    val bodySmallExtraBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    val bodySmallBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    val bodySmallSemiBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    val bodySmallMedium: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    val bodySmallRegular: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    val bodySmallLight: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 20.sp
    ),
    // Body/Xtra Small
    val bodyXtraSmallExtraBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 10.sp,
    ),
    val bodyXtraSmallBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
    ),
    val bodyXtraSmallSemiBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp
    ),
    val bodyXtraSmallMedium: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
    ),
    val bodyXtraSmallRegular: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    val bodyXtraSmallLight: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp
    ),
    // Body/Tiny
    val bodyTinyExtraBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 8.sp,
    ),
    val bodyTinyBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp
    ),
    val bodyTinySemiBold: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 8.sp
    ),
    val bodyTinyMedium: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 8.sp,
    ),
    val bodyTinyRegular: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp
    ),
    val bodyTinyLight: TextStyle = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 8.sp
    )
)