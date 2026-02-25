package com.example.core.ui.mapper

import com.example.core.domain.model.FilterOption
import com.example.core.ui.model.FilterOptionUi

fun FilterOption.toUiModel(category: FilterCategory,isSelected: Boolean = false): FilterOptionUi {
    return FilterOptionUi(
        key = this.key,
        label = this.label,
        category = category,
        iconRes = when (category) {
            FilterCategory.SORT -> FilterIconKeys.SORT
            FilterCategory.RATING -> FilterIconKeys.STAR
            FilterCategory.PROMO -> FilterIconKeys.PROMO
            FilterCategory.PRICE -> FilterIconKeys.PRICE
            FilterCategory.MODE -> if (key == "DELIVERY") FilterIconKeys.DELIVERY else FilterIconKeys.PICKUP
            else -> null
        },
        isSelected = isSelected,
    )
}

object FilterIconKeys {
    const val DELIVERY = "ic_delivery"
    const val PICKUP = "ic_pickup"
    const val STAR = "ic_star"
    const val PROMO = "ic_promo"
    const val SORT = "ic_sort"
    const val PRICE = "ic_money"
}

enum class FilterCategory { SORT, PRICE, RATING, PROMO, MODE, CUISINE }