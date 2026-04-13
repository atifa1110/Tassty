package com.example.tassty.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.core.ui.utils.FilterDataState
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.mapper.FilterIconKeys
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.R
import com.example.tassty.screen.category.FilterState
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange500

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

@Immutable
data class FilterCategoryDisplay(
    val category: FilterCategory,
    val defaultLabel: String,
    val iconRes: String,
    val selectedPalette: FilterPalette,
    val unselectedPalette: FilterPalette = FilterPalette(
        backgroundColor = Orange50,
        labelColor = Neutral100,
        iconColor = Neutral100,
        borderColor = Color.Transparent
    )
)

object FilterUiMapper {
    private val configs = mapOf(
        FilterCategory.SORT to FilterCategoryDisplay(
            category = FilterCategory.SORT,
            defaultLabel = "Sort",
            iconRes = FilterIconKeys.SORT,
            selectedPalette = FilterPalette(Orange50, Neutral100, Orange500, Orange200)
        ),
        FilterCategory.PRICE to FilterCategoryDisplay(
            category = FilterCategory.PRICE,
            defaultLabel = "Price",
            iconRes = FilterIconKeys.PRICE,
            selectedPalette = FilterPalette(Orange50, Neutral100, Orange500, Orange200)
        ),
        FilterCategory.RATING to FilterCategoryDisplay(
            category = FilterCategory.RATING,
            defaultLabel = "Rating",
            iconRes = FilterIconKeys.STAR,
            selectedPalette = FilterPalette(Orange500, Neutral10, Neutral10, Color.Transparent)
        ),
        FilterCategory.MODE to FilterCategoryDisplay(
            category = FilterCategory.MODE,
            defaultLabel = "Mode",
            iconRes = FilterIconKeys.DELIVERY,
            selectedPalette = FilterPalette(Green500, Neutral10, Neutral10, Color.Transparent)
        )
    )

    fun getConfig(category: FilterCategory) = configs[category]
        ?: throw IllegalArgumentException("Missing config for $category")

    fun getDrawable(iconKey: String?): Int? {
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
}

/**
 * Helper function untuk mapping active chips.
 * Pindahkan ke file terpisah agar bisa dipakai di SearchViewModel juga.
 */
fun mapToActiveFilters(
    state: FilterDataState,
    master: FilterState
): List<FilterOptionUi<FilterCategory>> {
    return listOf(FilterCategory.SORT, FilterCategory.RATING, FilterCategory.MODE, FilterCategory.PRICE).map { category ->
        val uiConfig = FilterUiMapper.getConfig(category)
        val appliedKey = state.applied[category]
        val masterItem = when(category) {
            FilterCategory.SORT -> master.sortList.find { it.key == appliedKey }
            FilterCategory.RATING -> master.ratingsOptions.find { it.key == appliedKey }
            FilterCategory.MODE -> master.modesOptions.find { it.key == appliedKey }
            FilterCategory.PRICE -> master.priceRanges.find { it.key == appliedKey }
            else -> null
        }
        FilterOptionUi(
            key = appliedKey ?: "default",
            label = masterItem?.label ?: uiConfig.defaultLabel,
            category = category,
            isSelected = appliedKey != null,
            iconRes = masterItem?.iconRes ?: uiConfig.iconRes
        )
    }
}