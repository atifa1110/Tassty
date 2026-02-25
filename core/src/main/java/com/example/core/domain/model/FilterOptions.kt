package com.example.core.domain.model

data class FilterOptions(
    val sortOptions: List<FilterOption>,
    val ratingOptions: List<FilterOption>,
    val priceRangeOptions: List<FilterOption>,
    val cuisineOptions: List<FilterOption>,
    val modeOptions: List<FilterOption>
)

enum class OptionType {
    RADIO,
    CHIP
}

data class FilterOption(
    val key: String,
    val label: String,
    val type: OptionType
)

