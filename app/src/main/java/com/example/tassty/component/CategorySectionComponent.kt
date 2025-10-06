package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.categories
import com.example.tassty.model.Category
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral100

@Composable
fun CategoryList(
    categories: List<Category>
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.explore_by_cuisine),
            style = LocalCustomTypography.current.h5Bold,
            color = Neutral100,
        )
        Spacer(Modifier.height(12.dp))
        CategoryStaggeredList(
            categories = categories,
            onCategoryClick = {}
        )
    }
}

@Composable
fun CategoryStaggeredList(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    // pisahin jadi atas & bawah
    val topRow = categories.filterIndexed { i, _ -> i % 2 == 0 }
    val bottomRow = categories.filterIndexed { i, _ -> i % 2 == 1 }

    val startingIndex = (categories.size / 2)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = startingIndex
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Baris atas
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(topRow) { category ->
                FoodCategoryCard(
                    category = category,
                    isSelected = false,
                    onCardClick = { onCategoryClick(category) }
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // Baris bawah
        LazyRow(
            state = listState, // pakai state sama biar scroll sync
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 32.dp) // offset dikit biar zigzag
        ) {
            items(bottomRow) { category ->
                FoodCategoryCard(
                    category = category,
                    isSelected = false,
                    onCardClick = { onCategoryClick(category) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    CategoryList(
        categories = categories
    )
}