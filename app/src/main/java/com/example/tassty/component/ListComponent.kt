package com.example.tassty.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tassty.R
import com.example.tassty.model.Category
import com.example.tassty.model.Filter
import com.example.tassty.ui.theme.LocalCustomTypography
import com.example.tassty.ui.theme.Neutral20
import com.example.tassty.ui.theme.Neutral70

@Composable
fun FoodCategoryGrid (
    searchQuery: String,
    filteredCategories: List<Category>,
    selectedCategoryIds: MutableList<Int>,
    modifier : Modifier = Modifier
){
    Column (
        modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        CategoryHeader(
            searchQuery = searchQuery,
            filteredCategories = filteredCategories,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredCategories) { category ->
                val isSelected = selectedCategoryIds.contains(category.id)
                FoodCategoryCard(
                    isSelected = isSelected,
                    imageUrl = category.imageUrl,
                    categoryName = category.name,
                    onCardClick = {
                        if(isSelected){
                            selectedCategoryIds.remove(category.id)
                        }else{
                            selectedCategoryIds.add(category.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FilterList(
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        Filter("Sort", R.drawable.arrow_down),
        Filter("Near me", R.drawable.location),
        Filter("Rated 4.5+", R.drawable.star),
        Filter("Promo Available", R.drawable.promo)
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            CustomFilterChip(filter)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFoodCategoryGrid() {
    FoodCategoryGrid(
        searchQuery = "",
        filteredCategories = listOf(
            Category(1, "","Salad"),
            Category(2,"","Burger"),
            Category(3,"","Pizza"),
        ),
        selectedCategoryIds = arrayListOf(1,2,3),
    )
}