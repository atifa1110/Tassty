package com.example.core.ui.model

import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.RestaurantStatus
import com.example.core.domain.utils.toCleanRupiahFormat

data class DetailMenuUiModel(
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
    val optionGroups: List<OptionGroupUiModel>,
    val restaurant: RestaurantUiModel,
    val menuStatus: MenuStatus,
    val isWishlist: Boolean
){
    val formatDeliveryCost : String get() = if(restaurant.deliveryCost==0) "Free" else "Paid"
    val formatPrice : String get() = priceOriginal.toCleanRupiahFormat()
    val formatPriceDiscount : String get() = if(promo) priceDiscount.toCleanRupiahFormat() else formatPrice
}

data class OptionGroupUiModel(
    val id: String,
    val title: String,
    val required: Boolean,
    val maxPick: Int,
    val options: List<OptionUiModel>
)

data class OptionUiModel(
    val id: String,
    val name: String,
    val extraPrice: Int,
    val extraPriceText: String,
    val isAvailable: Boolean,
    val isSelected: Boolean
)
