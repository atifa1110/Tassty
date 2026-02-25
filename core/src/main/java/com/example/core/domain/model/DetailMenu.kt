package com.example.core.domain.model

data class DetailMenu(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val promo: Boolean,
    val priceOriginal: Int,
    val priceDiscount: Int,
    val customizable: Boolean,
    val isAvailable: Boolean,
    val maxQuantity: Int,
    val stockStatus: String,
    val stockLabel: String,
    val optionGroups: List<OptionGroup>,
    val restaurant: Restaurant
){
    val menuStatus: MenuStatus = getMenuStatus(isAvailable,restaurant.statusResult.status)
}

data class OptionGroup(
    val id: String,
    val title: String,
    val required: Boolean,
    val maxPick: Int,
    val options: List<Option>
)

data class Option(
    val id: String,
    val name: String,
    val extraPrice: Int,
    val isAvailable: Boolean
)
