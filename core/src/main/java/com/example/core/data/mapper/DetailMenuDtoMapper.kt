package com.example.core.data.mapper

import com.example.core.data.model.DetailMenuDto
import com.example.core.data.model.OptionDto
import com.example.core.data.model.OptionGroupDto
import com.example.core.domain.model.DetailMenu
import com.example.core.domain.model.Option
import com.example.core.domain.model.OptionGroup

fun DetailMenuDto.toDomain() = DetailMenu(
    id = id,
    name = name,
    description = description?:"",
    imageUrl = imageUrl,
    promo = this.promo,
    priceOriginal = this.priceOriginal,
    priceDiscount = this.priceDiscount?:0,
    customizable = this.customizable,
    isAvailable = this.isAvailable,
    maxQuantity = this.maxQuantity?:0,
    stockStatus = this.stockStatus,
    stockLabel = this.stockLabel?:"",
    optionGroups = optionGroups.map { it.toDomain() },
    restaurant = this.restaurant?.toDomain()?: dummyRestaurant
)

fun OptionGroupDto.toDomain() = OptionGroup(
    id = id,
    title = title,
    required = required,
    maxPick = maxPick,
    options = options.map { it.toDomain() }
)

fun OptionDto.toDomain() = Option(
    id = id,
    name = name,
    extraPrice = extraPrice,
    isAvailable = isAvailable
)
