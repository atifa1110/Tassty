package com.example.tassty.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tassty.model.Category
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral40
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
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.size(20.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Orange500,      // Color when checked
                    uncheckedColor = Neutral40,      // Color when unchecked
                    checkmarkColor = Color.White     // Color of the checkmark
                )
            )
        }

        val annotatedText = buildAnnotatedString {
            append("I Agree with ")

            pushStringAnnotation(tag = "TOS", annotation = "terms_of_service")
            withStyle(style = SpanStyle(color = Orange500)) { // orange
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

@Composable
fun CategoryHeader(
    searchQuery: String,
    filteredCategories: List<Category>
) {
    val headerText = if (searchQuery.isBlank()) {
        "Categories"
    } else {
        if (filteredCategories.isEmpty()) "Category found"
        else "Category found"
    }

    val styledText: AnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Orange500,
            fontWeight = LocalCustomTypography.current.h4Bold.fontWeight,
            fontSize = LocalCustomTypography.current.h4Bold.fontSize,
            fontFamily = LocalCustomTypography.current.h4Bold.fontFamily
        ),
        ) {
            append("${filteredCategories.size} ")
        }
        withStyle(style = SpanStyle(
            color = Neutral100,
            fontWeight = LocalCustomTypography.current.h5Bold.fontWeight,
            fontSize = LocalCustomTypography.current.h5Bold.fontSize,
            fontFamily = LocalCustomTypography.current.h5Bold.fontFamily
        )
        ) {
            append(headerText)
        }
    }

    Text(text = styledText)
}