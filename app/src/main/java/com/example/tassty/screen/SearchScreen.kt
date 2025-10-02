package com.example.tassty.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tassty.ui.theme.Neutral10

@Composable
fun SearchScreen() {
    LazyColumn (modifier = Modifier.fillMaxSize().background(Neutral10)){
        item{

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
}