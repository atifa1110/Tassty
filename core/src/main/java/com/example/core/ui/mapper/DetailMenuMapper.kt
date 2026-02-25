package com.example.core.ui.mapper

import com.example.core.domain.model.DetailMenu
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Option
import com.example.core.domain.model.OptionGroup
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.OptionGroupUiModel
import com.example.core.ui.model.OptionUiModel

fun DetailMenu.toUiModel(isWishlist: Boolean) : DetailMenuUiModel{
    return DetailMenuUiModel(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        promo = this.promo,
        priceOriginal = this.priceOriginal,
        priceDiscount = this.priceDiscount,
        customizable = this.customizable,
        isAvailable = this.isAvailable,
        maxQuantity = this.maxQuantity,
        optionGroups = this.optionGroups.map { it.toDomain() },
        restaurant = this.restaurant.toUiModel(),
        menuStatus = this.menuStatus,
        isWishlist = isWishlist
    )
}

fun OptionGroup.toDomain() = OptionGroupUiModel(
    id = id,
    title = title,
    required = required,
    maxPick = maxPick,
    options = options.map { it.toDomain() }
)

fun Option.toDomain() = OptionUiModel(
    id = id,
    name = name,
    extraPrice = extraPrice,
    extraPriceText = "+${extraPrice.toCleanRupiahFormat()}",
    isAvailable = isAvailable,
    isSelected = false
)

fun DetailMenuUiModel.toDomain() : Menu{
    return Menu(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        price = if(this.promo)this.priceDiscount else this.priceOriginal,
        soldCount = 0,
        rank = this.restaurant.rank,
        customizable = this.customizable,
        isAvailable = this.isAvailable,
        maxQuantity = this.maxQuantity,
        stockStatus = "",
        stockLabel = "",
        restaurant = this.restaurant.toDomain(),
    )
}