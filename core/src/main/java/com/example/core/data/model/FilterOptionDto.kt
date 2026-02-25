package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class FilterOptionsDto(
    @SerializedName("sort_options")
    val sortOptions: List<FilterOptionDto>,
    @SerializedName("rating_options")
    val ratingOptions: List<FilterOptionDto>,
    @SerializedName("price_range_options")
    val priceOptions: List<FilterOptionDto>,
    @SerializedName("cuisine_options")
    val cuisineOptions: List<FilterOptionDto>,
    @SerializedName("mode_options")
    val modeOptions: List<FilterOptionDto>
)

data class FilterOptionDto(
    val key: String,
    val label: String
)