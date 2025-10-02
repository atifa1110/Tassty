package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral60

@Composable
fun StepIndicator(
    currentStep: Int,
    totalStep: Int
) {
    Text(
        text = buildAnnotatedString {
            // Current step → bold, hitam, font lebih besar
            withStyle(
                style = SpanStyle(
                    color = Neutral100,
                    fontSize = LocalCustomTypography.current.h5Bold.fontSize,
                    fontWeight = LocalCustomTypography.current.h5Bold.fontWeight,
                )
            ) {
                append(currentStep.toString())
            }

            append(" / ")

            // Total step → abu2, normal, font lebih kecil
            withStyle(
                style = SpanStyle(
                    color = Neutral60,
                    fontSize = LocalCustomTypography.current.h5Regular.fontSize,
                    fontWeight = LocalCustomTypography.current.h5Regular.fontWeight
                )
            ) {
                append(totalStep.toString())
            }
        },
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Neutral20) // bg abu2 muda
            .padding(horizontal = 16.dp, vertical = 4.dp)
    )
}
