package com.example.core.data.mapper

import com.example.core.data.model.FilterOptionDto
import com.example.core.data.model.FilterOptionsDto
import com.example.core.domain.model.FilterOption
import com.example.core.domain.model.FilterOptions
import com.example.core.domain.model.OptionType

fun FilterOptionsDto.toDomain(): FilterOptions {
    return FilterOptions(
        sortOptions = sort_options.map { it.toDomain(type = OptionType.RADIO) },
        discountOptions = discount_options.map { it.toDomain(type = OptionType.CHIP) },
        ratingOptions = rating_options.map { it.toDomain(type = OptionType.CHIP) },
        priceRangeOptions = price_range_options.map { it.toDomain(type = OptionType.RADIO) },
        cuisineOptions = cuisine_options.map { it.toDomain(type = OptionType.RADIO) },
        modeOptions = mode_options.map { it.toDomain(type = OptionType.CHIP) }
    )
}

fun FilterOptionDto.toDomain(type: OptionType): FilterOption {
    return FilterOption(
        key = key,
        label = label,
        type = type,
    )
}
