package com.example.core.data.model

data class FilterOptionsDto(
    val sort_options: List<FilterOptionDto>,
    val discount_options: List<FilterOptionDto>,
    val rating_options: List<FilterOptionDto>,
    val price_range_options: List<FilterOptionDto>,
    val cuisine_options: List<FilterOptionDto>,
    val mode_options: List<FilterOptionDto>
)

data class FilterOptionDto(
    val key: String,
    val label: String
)