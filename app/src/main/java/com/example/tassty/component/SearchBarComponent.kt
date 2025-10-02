package com.example.tassty.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Neutral30
import com.example.tassty.ui.theme.Neutral60
import com.example.tassty.ui.theme.Orange500

@Composable
fun SimpleSearchBar(
    text: String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = {onTextChange(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(
            modifier = Modifier.padding(2.dp),
            text = stringResource(R.string.search_for_something)
        ) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                Modifier.size(20.dp),
            )
        },
        textStyle = LocalCustomTypography.current.bodyMediumRegular,
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Neutral30,
            unfocusedTextColor = Neutral60,
            focusedTextColor = Neutral100,
            focusedBorderColor = Neutral30,
            focusedPlaceholderColor = Neutral60,
            unfocusedTrailingIconColor = Orange500,
            focusedTrailingIconColor = Orange500
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
   SimpleSearchBar("") { }
}