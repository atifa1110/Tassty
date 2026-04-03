package com.example.tassty.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500

@Composable
fun TermsOfServiceCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(0.dp).size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange500,
                    uncheckedColor = Neutral40,
                    checkmarkColor = Neutral10
                )
            )

        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = LocalCustomColors.current.headerText)) {
                append("I Agree with ")
            }

            pushStringAnnotation(tag = "TOS", annotation = "terms_of_service")
            withStyle(style = SpanStyle(color = Orange500)) {
                append("Terms of Service")
            }
            pop()
        }

        Text(
            text = annotatedText,
            style = LocalCustomTypography.current.bodyMediumSemiBold,
            modifier = Modifier.clickable {
                onTermsClick()
            }
        )
    }
}