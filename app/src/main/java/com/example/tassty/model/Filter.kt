package com.example.tassty.model

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.core.ui.utils.FilterDataState
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.mapper.FilterIconKeys
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.R
import com.example.tassty.screen.category.FilterState
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange500

fun getFilterDrawable(iconKey: String?): Int? {
    return when (iconKey) {
        FilterIconKeys.SORT -> R.drawable.arrow_down
        FilterIconKeys.DELIVERY -> R.drawable.delivery
        FilterIconKeys.PICKUP -> R.drawable.hand
        FilterIconKeys.STAR -> R.drawable.star
        FilterIconKeys.PROMO -> R.drawable.promo
        FilterIconKeys.PRICE -> R.drawable.money
        else -> null
    }
}

fun getFilterPalette(category: FilterCategory): FilterPalette {
    return when (category) {
        FilterCategory.SORT -> FilterPalette(
            backgroundColor = Orange50,
            labelColor = Neutral100,
            iconColor = Orange500,
            borderColor = Orange200
        )

        FilterCategory.PRICE -> FilterPalette(
            backgroundColor = Orange50,
            labelColor = Neutral100,
            iconColor = Orange500,
            borderColor = Orange200
        )

        FilterCategory.RATING -> FilterPalette(
            backgroundColor = Orange500,
            labelColor = Neutral10,
            iconColor = Neutral10,
            borderColor = Color.Transparent
        )

        FilterCategory.PROMO -> FilterPalette(
            backgroundColor = Blue500,
            labelColor = Neutral10,
            iconColor = Neutral10,
            borderColor = Color.Transparent
        )

        FilterCategory.MODE -> FilterPalette(
            backgroundColor = Green500,
            labelColor = Neutral10,
            iconColor = Neutral10,
            borderColor = Color.Transparent
        )

        FilterCategory.CUISINE -> FilterPalette(
            backgroundColor = Orange50,
            labelColor = Neutral100,
            iconColor = Neutral100,
            borderColor = Color.Transparent
        )
    }
}

data class FilterPalette(
    val backgroundColor: Color,
    val labelColor: Color,
    val iconColor: Color,
    val borderColor: Color
)

data class ChipFilterOption(
    val label: String,
    val iconId: Int? = null,
    val key: String = ""
)

/**
 * Helper function untuk mapping active chips.
 * Pindahkan ke file terpisah agar bisa dipakai di SearchViewModel juga.
 */
fun mapToActiveFilters(filterData: FilterDataState, data: FilterState): List<FilterOptionUi<FilterCategory>> {
    return data.activeFilters.map { placeholder ->
        when (placeholder.category) {
            FilterCategory.SORT -> {
                val applied = data.sortList.find { it.key == filterData.appliedSort }
                Log.d("FILTER_DEBUG", "Applied key: ${filterData.appliedSort} | Found label: ${applied?.label}")
                placeholder.copy(label = applied?.label ?: "Sort", isSelected = applied != null)
            }
            FilterCategory.PRICE -> {
                val applied = data.priceRanges.find { it.key == filterData.appliedPrice }
                placeholder.copy(label = applied?.label ?: "Price", isSelected = applied != null)
            }
            FilterCategory.RATING -> {
                val applied = data.ratingsOptions.find { it.key == filterData.appliedRating }
                placeholder.copy(label = applied?.label ?: "Rating", isSelected = applied != null)
            }
            FilterCategory.MODE -> {
                val applied = data.modesOptions.find { it.key == filterData.appliedMode }
                placeholder.copy(label = applied?.label ?: "Mode", isSelected = applied != null)
            }
            FilterCategory.CUISINE -> {
                val applied = data.cuisineOptions.find { it.key == filterData.appliedCuisine }
                placeholder.copy(label = applied?.label ?: "Cuisine", isSelected = applied != null)
            }
            else -> placeholder
        }
    }
}