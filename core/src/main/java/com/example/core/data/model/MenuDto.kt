package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class MenuDto(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: Int? = 0,

    @SerializedName("sold_count")
    val soldCount: Int? = 0,

    @SerializedName("rank")
    val rank: Int? = null,

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

    @SerializedName("restaurant")
    val restaurant: RestaurantShortDto? = null
)


data class MenuShortDto(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("price")
    val price: Int? = 0,

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
)
