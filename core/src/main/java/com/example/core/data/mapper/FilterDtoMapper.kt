package com.example.core.data.mapper

import com.example.core.data.model.FilterOptionDto
import com.example.core.data.model.FilterOptionsDto
import com.example.core.domain.model.FilterOption
import com.example.core.domain.model.FilterOptions
import com.example.core.domain.model.OptionType

fun FilterOptionsDto.toDomain(): FilterOptions {
    return FilterOptions(
        sortOptions = sortOptions.map { it.toDomain(type = OptionType.RADIO) },
        ratingOptions = ratingOptions.map { it.toDomain(type = OptionType.CHIP) },
        priceRangeOptions = priceOptions.map { it.toDomain(type = OptionType.RADIO) },
        cuisineOptions = cuisineOptions.map { it.toDomain(type = OptionType.RADIO) },
        modeOptions = modeOptions.map { it.toDomain(type = OptionType.CHIP) }
    )
}

fun FilterOptionDto.toDomain(type: OptionType): FilterOption {
    return FilterOption(
        key = key,
        label = label,
        type = type,
    )
}
