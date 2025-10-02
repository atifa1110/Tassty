package com.example.tassty.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.categoriesItem
import com.example.tassty.model.Category
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange50

@Composable
fun CategoryCard(
    category: Category
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = Orange200,
                shape = CircleShape
            )
            .background(Orange50)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
       CategoryImageCircle(category = category)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
    CategoryCard(
        category = categoriesItem
    )
}