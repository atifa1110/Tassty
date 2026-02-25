package com.example.tassty.component

import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange50
import com.example.core.domain.model.Category
import com.example.tassty.categories
import com.example.tassty.ui.theme.Orange500

@Composable
fun CategoryCard(
    category: CategoryUiModel,
    onClick:() -> Unit
) {
    Box(modifier = Modifier.clickable(onClick = onClick)
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = if(category.isSelected) Orange500 else Orange200,
                shape = CircleShape
            )
            .background(if(category.isSelected) Orange500 else Orange50)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
       CategoryImageCircle(
           categoryName = category.name,
           imageUrl=category.imageUrl
       )
    }
}

@Composable
fun CategoryCard(
    title: String,
    image: String,
    onClick:() -> Unit
) {
    Box(modifier = Modifier.clickable(onClick = onClick)
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
        CategoryImageCircle(
            categoryName =title,
            imageUrl=image
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryScreen() {
    CategoryCard(
        category = categories[0],
        onClick = {}
    )
}