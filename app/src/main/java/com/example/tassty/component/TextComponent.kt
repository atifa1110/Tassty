package com.example.tassty.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.Pink600

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    textError: String = "",
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 5,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Neutral100,
            unfocusedTextColor = Neutral100,
            focusedPlaceholderColor = Neutral60,
            unfocusedPlaceholderColor = Neutral60,
            focusedContainerColor = Neutral10,
            unfocusedContainerColor = Neutral10,
            focusedBorderColor = Neutral30,
            unfocusedBorderColor = Neutral30,
            unfocusedLeadingIconColor = Neutral70,
            focusedLeadingIconColor = Neutral100,
            unfocusedTrailingIconColor = Neutral60,
            focusedTrailingIconColor = Neutral60,
            errorPlaceholderColor = Pink600,
            errorTextColor = Pink600,
            errorBorderColor = Pink600,
            errorContainerColor = Pink50,
            errorLeadingIconColor = Pink600,
            errorTrailingIconColor = Pink600
        ),
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        visualTransformation = visualTransformation,
        value = text,
        onValueChange = { onTextChanged(it) },
        placeholder = {
            Text(
                text = placeholder,
                style = LocalCustomTypography.current.bodyMediumRegular
            )
        },
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        isError = textError.isNotEmpty(),
    )
    if(textError.isNotEmpty()){
        TextFieldError(textError = textError)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.search_for_something),
    enabled: Boolean = true,
    isTransparentMode: Boolean = false // Flag untuk bedain Home vs White section
) {
    val containerColor = if (isTransparentMode) Neutral10.copy(alpha = 0.10f) else Neutral10
    val borderColor = if (isTransparentMode) Color.White.copy(alpha = 0.32f) else Neutral30
    val iconColor = if (isTransparentMode) Neutral10 else Orange500
    val textColor = if (isTransparentMode) Neutral10 else Neutral100
    val placeholderColor = if (isTransparentMode) Neutral10.copy(alpha = 0.50f) else Neutral60

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(99.dp)
            ),
        shape = RoundedCornerShape(99.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalCustomTypography.current.bodyMediumMedium.copy(color = textColor),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                disabledTextColor = textColor.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun CollectionText(
    modifier: Modifier = Modifier,
    itemCount: Int
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h6Bold.toSpanStyle().copy(color = Neutral100)) {
                append("$itemCount")
            }
            withStyle(style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle().copy(color = Neutral70)) {
                append(" menus")
            }
        },
        modifier = modifier
    )
}

@Composable
fun StepIndicatorText(
    currentStep: Int,
    totalStep: Int
) {
    Text(
        text = buildAnnotatedString {
            // Current step
            withStyle(style = LocalCustomTypography.current.h5Bold.toSpanStyle().copy(color = Neutral100)) {
                append(currentStep.toString())
            }
            // Total step
            withStyle(style = LocalCustomTypography.current.h5Regular.toSpanStyle().copy(color = Neutral60)) {
                append(" / $totalStep")
            }

        },
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Neutral20)
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
    val color = if (isDiscount) Pink500 else Neutral70
    val formattedPrice = if (isDiscount) "-$price" else price

    BasePriceText(
        price = formattedPrice,
        modifier = modifier,
        primaryStyle = LocalCustomTypography.current.h6Bold,
        secondaryStyle = LocalCustomTypography.current.h8Regular,
        primaryColor =color,
        secondaryColor = color
    )
}

@Composable
fun FoodPriceText(
    modifier: Modifier = Modifier,
    price: String, // Contoh: "Rp20.000" atau "Rp0"
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
                                .copy(color = Neutral100)
                        ) {
                            append(label)
                        }

                        withStyle(
                            style = LocalCustomTypography.current.bodySmallMedium.toSpanStyle()
                                .copy(color = Neutral70)
                        ) {
                            append(data)
                        }
                    }
                )
            } else {
                Text(
                    text = line,
                    style = LocalCustomTypography.current.bodySmallMedium
                        .copy(color = Neutral70)
                )
            }
        }
    }
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
