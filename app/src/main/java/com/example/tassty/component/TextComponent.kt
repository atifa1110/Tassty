package com.example.tassty.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Neutral70
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink600

@Composable
fun TextComponent(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    leadingIcon: Int? = null,
    textError: String = "",
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Neutral30,
            unfocusedBorderColor = Neutral30,
            focusedPlaceholderColor = Neutral100,
            unfocusedPlaceholderColor = Neutral60,
            unfocusedLeadingIconColor = Neutral70,
            focusedLeadingIconColor = Neutral100,
            errorPlaceholderColor = Pink600,
            errorTextColor = Pink600,
            errorBorderColor = Pink600,
            errorContainerColor = Pink50,
            errorLeadingIconColor = Pink600,
            errorTrailingIconColor = Pink600
        ),
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        value = text,
        onValueChange = { onTextChanged(it) },
        placeholder = { Text(text = placeholder,
            style = LocalCustomTypography.current.bodyMediumRegular,) },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = placeholder,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        singleLine = true,
        isError = textError.isNotEmpty(),
    )
    if(textError.isNotEmpty()){
        TextFieldError(R.string.text_error)
    }
}

@Composable
fun TextComponentNoIcon(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholder: String,
    textError: String = "",
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Neutral30,
            unfocusedBorderColor = Neutral30,
            focusedPlaceholderColor = Neutral100,
            unfocusedPlaceholderColor = Neutral60,
            unfocusedLeadingIconColor = Neutral70,
            focusedLeadingIconColor = Neutral100,
            errorPlaceholderColor = Pink600,
            errorTextColor = Pink600,
            errorBorderColor = Pink600,
            errorContainerColor = Pink50,
            errorLeadingIconColor = Pink600,
            errorTrailingIconColor = Pink600
        ),
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        value = text,
        onValueChange = { onTextChanged(it) },
        placeholder = { Text(text = placeholder,
            style = LocalCustomTypography.current.bodyMediumRegular,) },
        singleLine = true,
        isError = textError.isNotEmpty(),
    )
    if(textError.isNotEmpty()){
        TextFieldError(R.string.text_error)
    }
}

@Composable
fun SearchBarHomeSection(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.search_delicacies),
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .border(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.32f),
            shape = RoundedCornerShape(99.dp)
        ),
        shape = RoundedCornerShape(99.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10.copy(alpha = 0.10f)
        ),
        // Elevation rendah untuk shadow ringan (PENTING untuk performa)
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = {
                Text(
                    text = placeholder,
                    style = LocalCustomTypography.current.bodyMediumRegular,
                    color = Neutral10.copy(alpha = 0.50f)
                )
            },
            textStyle = LocalCustomTypography.current.bodyMediumRegular,
            // Ikon kaca pembesar (di sebelah kanan)
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = Neutral10
                )
            },
            modifier = Modifier.fillMaxWidth(),
            // Menonaktifkan indikator/border default TextField
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Neutral10.copy(alpha = 0.10f),
                unfocusedContainerColor = Neutral10.copy(alpha = 0.10f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedTextColor = Neutral10,
                focusedTextColor = Neutral10
            ),
        )
    }
}

@Composable
fun SearchBarWhiteSection(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.search_for_something),
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.border(
            width = 1.dp,
            color = Neutral30, // warna border
            shape = RoundedCornerShape(99.dp) // sama kayak shape Card
        ),
        shape = RoundedCornerShape(99.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        ),
        // Elevation rendah untuk shadow ringan (PENTING untuk performa)
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = {
                Text(
                    text = placeholder,
                    style = LocalCustomTypography.current.bodyMediumRegular,
                )
            },
            textStyle = if (value.isNotEmpty()) {
                LocalCustomTypography.current.bodyMediumMedium
            } else {
                LocalCustomTypography.current.bodyMediumRegular
            },
            // Ikon kaca pembesar (di sebelah kanan)
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = Orange500
                )
            },
            modifier = Modifier.fillMaxWidth(),

            // Menonaktifkan indikator/border default TextField
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Neutral10,
                unfocusedContainerColor = Neutral10,
                unfocusedTextColor = Neutral100,
                focusedTextColor = Neutral100,
                unfocusedPlaceholderColor = Neutral60,
                focusedPlaceholderColor = Neutral60,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
        )
    }
}

@Composable
fun NotesBarSection(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = stringResource(R.string.places_notes_here),
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .border(
            width = 1.dp,
            color = Neutral30, // warna border
            shape = RoundedCornerShape(99.dp)
        ),
        shape = RoundedCornerShape(99.dp),
        colors = CardDefaults.cardColors(
            containerColor = Neutral10
        )) {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = {
                Text(
                    text = placeholder,
                    style = LocalCustomTypography.current.bodyMediumRegular,
                )
            },
            textStyle = if (value.isNotEmpty()) {
                LocalCustomTypography.current.bodyMediumMedium
            } else {
                LocalCustomTypography.current.bodyMediumRegular
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.clipboard_list),
                    contentDescription = "notes",
                    tint = Neutral70,
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Neutral10,
                unfocusedContainerColor = Neutral10,
                unfocusedTextColor = Neutral100,
                focusedTextColor = Neutral100,
                unfocusedPlaceholderColor = Neutral60,
                focusedPlaceholderColor = Neutral60,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
        )
    }
}

@Composable
fun TitleListHeader(
    data: Int,
    text: String
){
    Row(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = data.toString(),
            color = Orange500,
            style = LocalCustomTypography.current.h4Bold
        )
        Text(
            text = text,
            color = Neutral100,
            style = LocalCustomTypography.current.h5Bold
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
fun FoodPriceText(
    modifier: Modifier = Modifier,
    price: String,
    currency: String = "Rp",
    color: Color
) {
    val parts = price.split(".")
    Text(
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h5Bold.toSpanStyle().copy(color = color)) {
                append(parts[0])
            }
            withStyle(style = LocalCustomTypography.current.h7Regular.toSpanStyle().copy(color = color)) {
                append(".${parts[1]}")
            }
        },
        modifier = modifier
    )
}

@Composable
fun FoodPriceTinyText(
    modifier: Modifier = Modifier,
    price: String,
    currency: String = "Rp",
) {
    val parts = price.split(".")
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h7Bold.toSpanStyle().copy(color = Orange500)) {
                append("$currency${parts[0]}")
            }
            withStyle(style = LocalCustomTypography.current.h8Regular.toSpanStyle().copy(color = Orange500)) {
                append(".${parts[1]}")
            }
        },
    )
}

@Composable
fun FoodPriceBigText(
    modifier: Modifier = Modifier,
    price: String,
    currency: String = "Rp",
    color: Color
) {
    val parts = price.split(".")
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h3Bold.toSpanStyle().copy(color = color)) {
                append("$currency${parts[0]}")
            }
            withStyle(style = LocalCustomTypography.current.h4Regular.toSpanStyle().copy(color = color)) {
                append(".${parts[1]}")
            }
        }
    )
}

@Composable
fun FoodPriceLineText(
    modifier: Modifier = Modifier,
    price: String,
    currency: String = "Rp",
    color: Color
) {
    val parts = price.split(".")
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = LocalCustomTypography.current.h5Regular.toSpanStyle().copy(color = color)) {
                append("$currency${parts[0]}")
            }
            withStyle(style = LocalCustomTypography.current.h7Regular.toSpanStyle().copy(color = color)) {
                append(".${parts[1]}")
            }
        },
        textDecoration = TextDecoration.LineThrough
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

        TextComponentNoIcon(
            text = "",
            onTextChanged = {},
            placeholder = "No Icon"
        )

        SearchBarHomeSection(
            value = "",
            onValueChange = {}
        )

        SearchBarWhiteSection(
            value = "",
            onValueChange = {}
        )

        NotesBarSection(
            value = "",
            onValueChange = {}
        )

        TitleListHeader(data = 16, text = "Restaurant")

        FoodPriceText(price = "25.000", color = Neutral100)

        FoodPriceTinyText(price = "25.000")

        FoodPriceBigText(price = "25.000", color = Orange500)

        FoodPriceLineText(price = "25.000", color = Neutral60)
    }
}
