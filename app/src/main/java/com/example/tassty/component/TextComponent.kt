package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.search_for_something),
    enabled: Boolean = true,
    isTransparentMode: Boolean = false
) {
    val containerColor = if (isTransparentMode) LocalCustomColors.current.searchBackground else LocalCustomColors.current.background
    val borderColor = if (isTransparentMode) LocalCustomColors.current.border else LocalCustomColors.current.border
    val iconColor = if (isTransparentMode) Neutral10 else Orange500
    val textColor = if (isTransparentMode) Neutral10 else LocalCustomColors.current.headerText
    val placeholderColor = if (isTransparentMode) Neutral10 else Neutral60

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = LocalCustomTypography.current.bodyMediumRegular,
                color = placeholderColor
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = iconColor
            )
        },
        textStyle = LocalCustomTypography.current.bodyMediumMedium.copy(color = textColor),
        shape = RoundedCornerShape(99.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedBorderColor = if (isTransparentMode) Color.White else Orange500,
            unfocusedBorderColor = borderColor,
            disabledBorderColor = borderColor.copy(alpha = 0.5f),
            cursorColor = if (isTransparentMode) Color.White else Orange500
        ),
        singleLine = true
    )
}

//@Composable
//fun SearchBar(
//    modifier: Modifier = Modifier,
//    value: String = "",
//    onValueChange: (String) -> Unit,
//    placeholder: String = stringResource(R.string.search_for_something),
//    enabled: Boolean = true,
//    isTransparentMode: Boolean = false
//) {
//    val containerColor = if (isTransparentMode) Neutral10.copy(alpha = 0.10f) else LocalCustomColors.current.background
//    val borderColor = if (isTransparentMode) Color.White.copy(alpha = 0.32f) else LocalCustomColors.current.border
//    val iconColor = if (isTransparentMode) Neutral10 else Orange500
//    val textColor = if (isTransparentMode) Neutral10 else LocalCustomColors.current.headerText
//    val placeholderColor = if (isTransparentMode) Neutral10.copy(alpha = 0.50f) else Neutral60
//
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .border(
//                width = 1.dp,
//                color = borderColor,
//                shape = RoundedCornerShape(99.dp)
//            ),
//        shape = RoundedCornerShape(99.dp),
//        colors = CardDefaults.cardColors(containerColor = containerColor),
//        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
//    ) {
//        TextField(
//            value = value,
//            onValueChange = onValueChange,
//            enabled = enabled,
//            placeholder = {
//                Text(
//                    text = placeholder,
//                    style = LocalCustomTypography.current.bodyMediumRegular,
//                    color = placeholderColor
//                )
//            },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = null,
//                    tint = iconColor
//                )
//            },
//            modifier = Modifier.fillMaxWidth(),
//            textStyle = LocalCustomTypography.current.bodyMediumMedium.copy(color = textColor),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                disabledContainerColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                focusedTextColor = textColor,
//                unfocusedTextColor = textColor,
//                disabledTextColor = textColor.copy(alpha = 0.5f)
//            )
//        )
//    }
//}

@Composable
fun CollectionText(
    modifier: Modifier = Modifier,
    itemCount: Int
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h6Bold.toSpanStyle().copy(color = LocalCustomColors.current.headerText)) {
                append("$itemCount")
            }
            withStyle(style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle().copy(color = LocalCustomColors.current.text)) {
                append(" menus")
            }
        },
        modifier = modifier,
        style = LocalCustomTypography.current.bodySmallMedium
    )
}

@Composable
fun StepIndicatorText(
    currentStep: Int,
    totalStep: Int
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h5Bold.toSpanStyle().copy(color = LocalCustomColors.current.headerText)) {
                append(currentStep.toString())
            }
            withStyle(style = LocalCustomTypography.current.h5Regular.toSpanStyle().copy(color = Neutral60)) {
                append(" / $totalStep")
            }

        },
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(LocalCustomColors.current.cardBackground)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
private fun BasePriceText(
    price: String,
    modifier: Modifier,
    primaryStyle: androidx.compose.ui.text.TextStyle,
    secondaryStyle: androidx.compose.ui.text.TextStyle,
    primaryColor: Color,
    secondaryColor: Color,
    textDecoration: TextDecoration? = null
) {
    // Logika parsing yang lebih aman untuk memisahkan Rupiah dan angka ribuan
    // Ex: "Rp10.000" -> primaryPart: "Rp10", secondaryPart: ".000"

    // Cari index titik (dot) terakhir. Jika tidak ada, price dianggap sebagai primary part.
    val lastDotIndex = price.lastIndexOf('.')

    val primaryPart: String
    val secondaryPart: String

    if (lastDotIndex != -1 && price.length > lastDotIndex + 1) {
        // Jika ada dot terakhir dan diikuti karakter (misal: "Rp10.000")
        primaryPart = price.substring(0, lastDotIndex) // "Rp10"
        secondaryPart = price.substring(lastDotIndex) // ".000"
    } else {
        // Jika tidak ada dot atau hanya satu angka ("Rp10")
        primaryPart = price
        secondaryPart = ""
    }

    Text(
        text = buildAnnotatedString {
            withStyle(style = primaryStyle.toSpanStyle().copy(color = primaryColor)) {
                append(primaryPart)
            }
            if (secondaryPart.isNotEmpty()) {
                withStyle(style = secondaryStyle.toSpanStyle().copy(color = secondaryColor)) {
                    append(secondaryPart)
                }
            }
        },
        modifier = modifier,
        textDecoration = textDecoration
    )
}

@Composable
fun SummaryPriceText(
    modifier: Modifier = Modifier,
    price: String,
    isDiscount:Boolean,
) {
    val color = if (isDiscount) Pink500 else LocalCustomColors.current.text
    val formattedPrice = if (isDiscount) "-$price" else price

    BasePriceText(
        price = formattedPrice,
        modifier = modifier,
        primaryStyle = LocalCustomTypography.current.h6Bold,
        secondaryStyle = LocalCustomTypography.current.h8Regular,
        primaryColor = color,
        secondaryColor = color
    )
}

@Composable
fun FoodPriceText(
    modifier: Modifier = Modifier,
    price: String,
    color: Color
) {
    BasePriceText(
        price = price,
        modifier = modifier,
        primaryStyle = LocalCustomTypography.current.h5Bold,
        secondaryStyle = LocalCustomTypography.current.h7Regular,
        primaryColor = color ,
        secondaryColor = color
    )
}

@Composable
fun FoodPriceTinyText(
    modifier: Modifier = Modifier,
    price: String
) {
    BasePriceText(
        modifier = modifier,
        price = price,
        primaryStyle = LocalCustomTypography.current.h7Bold,
        secondaryStyle = LocalCustomTypography.current.h8Regular,
        primaryColor = Orange500,
        secondaryColor = Orange500
    )
}

@Composable
fun FoodPriceBigText(
    modifier: Modifier = Modifier,
    price: String,
    color: Color
) {
    BasePriceText(
        price = price,
        modifier = modifier,
        primaryStyle = LocalCustomTypography.current.h3Bold,
        secondaryStyle = LocalCustomTypography.current.h4Regular,
        primaryColor = color ,
        secondaryColor = color
    )
}

@Composable
fun FoodPriceLineText(
    modifier: Modifier = Modifier,
    price: String,
    color: Color
) {
    BasePriceText(
        price = price,
        modifier = modifier,
        primaryStyle = LocalCustomTypography.current.h5Bold,
        secondaryStyle = LocalCustomTypography.current.h7Regular,
        primaryColor = color ,
        secondaryColor = color,
        textDecoration = TextDecoration.LineThrough
    )

}

@Composable
fun NotesText(
    notes: String
) {
    val lines = notes.split("\n")

    Column(modifier = Modifier) {
        lines.filter { it.isNotBlank() }.forEach { line ->
            val parts = line.split(":", limit = 2)

            if (parts.size == 2) {
                val label = parts[0].trim() + " : "
                val data = parts[1].trim()

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle()
                                .copy(color = LocalCustomColors.current.headerText)
                        ) {
                            append(label)
                        }

                        withStyle(
                            style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle()
                                .copy(color = LocalCustomColors.current.text)
                        ) {
                            append(data)
                        }
                    }
                )
            } else {
                Text(
                    color = LocalCustomColors.current.text,
                    text = line,
                    style = LocalCustomTypography.current.bodySmallMedium
                )
            }
        }
    }
}

@Composable
fun CustomTwoColorText(
    modifier: Modifier = Modifier,
    fullText: String,
    highlightText: String,
    highlightColor: Color = Orange500,
    textColor: Color = LocalCustomColors.current.text,
    normalStyle: TextStyle = LocalCustomTypography.current.bodySmallRegular,
    textAlign: TextAlign = TextAlign.Center,
    onHighlightClick: (() -> Unit)? = null
) {
    val annotatedString = buildAnnotatedString {
        val startIndex = fullText.indexOf(highlightText)

        if (startIndex != -1) {
            append(fullText.substring(0, startIndex))

            val link = if (onHighlightClick != null) {
                LinkAnnotation.Clickable(
                    tag = "highlight_link",
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = highlightColor,
                        )
                    ),
                    linkInteractionListener = { onHighlightClick() }
                )
            } else null

            if (link != null) {
                withLink(link) {
                    append(highlightText)
                }
            } else {
                withStyle(style = SpanStyle(color = highlightColor)) {
                    append(highlightText)
                }
            }

            append(fullText.substring(startIndex + highlightText.length))
        } else {
            append(fullText)
        }
    }

    Text(
        modifier = modifier.fillMaxWidth(),
        text = annotatedString,
        style = normalStyle,
        textAlign = textAlign,
        color = textColor
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAllComponents() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextComponent(
            text = "",
            onTextChanged = {},
            placeholder = "With Icon",
            leadingIcon = R.drawable.star
        )

        SearchBar(
            value = "",
            onValueChange = {  },
            placeholder = stringResource(R.string.search_delicacies),
            isTransparentMode = true,
            enabled = false
        )

        SearchBar(
            value = "",
            onValueChange = {} ,
            isTransparentMode = false
        )

        FoodPriceText(price = "Rp25.000", color = Neutral100)

        FoodPriceTinyText(price = "Rp25.000")

        FoodPriceBigText(price = "Rp25.000", color = Orange500)

        FoodPriceLineText(price = "Rp25.000", color = Neutral60)

    }
}
