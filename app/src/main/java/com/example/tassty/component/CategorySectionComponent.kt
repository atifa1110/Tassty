package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import com.example.core.ui.model.CategoryUiModel
import com.example.tassty.R
import com.example.tassty.ui.theme.LocalCustomColors
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.util.RestaurantPreviewData

@Composable
fun CategoryList(
    categories: List<CategoryUiModel>
){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.explore_by_cuisine),
            style = LocalCustomTypography.current.h5Bold,
            color = LocalCustomColors.current.headerText
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
    categories: List<CategoryUiModel>,
    onCategoryClick: (CategoryUiModel) -> Unit
) {
    val rows = categories.chunked(2)
    val middleIndex = rows.size / 2

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = middleIndex)

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(rows) { pair ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
            ) {
                FoodCategoryCard(
                    category = pair[0],
                    isSelected = false,
                    onCardClick = { onCategoryClick(pair[0]) }
                )

                if (pair.size > 1) {
                    FoodCategoryCard(
                        category = pair[1],
                        isSelected = false,
                        onCardClick = { onCategoryClick(pair[1]) },
                        modifier = Modifier.offset(x = 28.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ShimmerCategoryStaggeredList() {
    val shimmerItems = List(10) { it }
    val rows = shimmerItems.chunked(2)
    val middleIndex = rows.size / 2

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = middleIndex)

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(rows) { pair ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ShimmerFoodCategoryCard()
                if (pair.size > 1) {
                    ShimmerFoodCategoryCard(
                        modifier = Modifier.offset(x = 28.dp)
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    CategoryList(
        categories = RestaurantPreviewData.categoriesUiModel
    )
}