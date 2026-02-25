package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class DetailMenuDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String ?= "",

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("promo")
    val promo: Boolean = false,

    @SerializedName("price_original")
    val priceOriginal: Int,

    @SerializedName("price_discount")
    val priceDiscount: Int? = 0,

    @SerializedName("customizable")
    val customizable: Boolean = false,

    @SerializedName("is_available")
    val isAvailable: Boolean = false,

    @SerializedName("max_quantity")
    val maxQuantity: Int? = 0,

    @SerializedName("stock_status")
    val stockStatus: String = "",

    @SerializedName("stock_label")
    val stockLabel: String? = null,

    @SerializedName("option_groups")
    val optionGroups: List<OptionGroupDto>,

    @SerializedName("restaurant")
    val restaurant: RestaurantShortDto? = null
)

data class OptionGroupDto(
    val id: String,
    val title: String,
    val required: Boolean,
    @SerializedName("max_pick")
    val maxPick: Int,
    val options: List<OptionDto>
)

data class OptionDto(
    val id: String,
    val name: String,
    @SerializedName("extra_price")
    val extraPrice: Int,
    @SerializedName("is_available")
    val isAvailable: Boolean
)


