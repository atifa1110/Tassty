package com.example.tassty.model

import androidx.compose.ui.graphics.Color
import com.example.core.domain.model.FilterOption
import com.example.core.domain.model.OptionType
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.R
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange500

enum class ChipType(
    val defaultLabel: String,
    val defaultIcon: Int?,
    val selectedColor: Color,
    val selectedLabelColor: Color,
    val selectedIconColor: Color,
    val selectedBorderColor: Color
) {
    SORT(
        defaultLabel = "Sort",
        defaultIcon = R.drawable.arrow_down,
        selectedColor = Orange50,
        selectedLabelColor = Neutral100,
        selectedIconColor = Orange500,
        selectedBorderColor = Orange200,
    ),
    RATING(
        defaultLabel = "Rating",
        defaultIcon = R.drawable.star,
        selectedColor = Orange500,
        selectedLabelColor = Color.White,
        selectedIconColor = Color.White,
        selectedBorderColor = Color.Transparent
    ),
    PROMO(
        defaultLabel = "Promo",
        defaultIcon = R.drawable.promo,
        selectedColor = Blue500,
        selectedLabelColor = Color.White,
        selectedIconColor = Color.White,
        selectedBorderColor = Color.Transparent
    ),
    DELIVERY(
        defaultLabel = "Delivery",
        defaultIcon = R.drawable.hand,
        selectedColor = Green500,
        selectedLabelColor = Color.White,
        selectedIconColor = Color.White,
        selectedBorderColor = Color.Transparent
    ),

    PRICE(
        defaultLabel = "Price",
        defaultIcon = null,
        selectedColor = Orange50,
        selectedLabelColor = Neutral100,
        selectedIconColor = Orange500,
        selectedBorderColor = Orange200,
    ),

    CUISINE(
        defaultLabel = "Cuisine",
        defaultIcon = null,
        selectedColor = Orange50,
        selectedLabelColor = Neutral100,
        selectedIconColor = Orange500,
        selectedBorderColor = Orange200,
    ),
}

data class SummaryFilterChip(
    val type: ChipType,
    val key: String,
    val label: String,
    val isSelected: Boolean,
    val icon: Int?
)

sealed interface FilterKey {
    data object PriceRange : FilterKey
    data object RestoRating : FilterKey
    data object Discount : FilterKey
    data object Mode : FilterKey
    data object Cuisine : FilterKey
}

data class FilterState(
    val priceRange: FilterOptionUi? = null,
    val restoRating: FilterOptionUi? = null,
    val discounts: FilterOptionUi? = null,
    val mode: FilterOptionUi? = null,
    val cuisine: FilterOptionUi? = null
) {
    companion object
}

fun FilterOption.toUi(): FilterOptionUi {
    return FilterOptionUi(
        key = key,
        label = label,
        iconRes = if (type == OptionType.CHIP) key.toChipIconRes() else null,
        isSelected = false
    )
}

fun String.toChipIconRes(): Int? {
    return when (this.uppercase()) {
        "DELIVERY" -> R.drawable.hand
        "PICKUP" -> R.drawable.hand
        "ALL" -> R.drawable.promo
        "CASHBACK" -> R.drawable.icon
        "DISCOUNT" -> R.drawable.promo
        "SHIPPING" -> R.drawable.promo
        "4.5_UP" -> R.drawable.star
        "4.0_UP" -> R.drawable.star
        else -> null
    }
}

data class ChipFilterOption(
    val label: String,
    val iconId: Int? = null,
    val key: String = ""
)