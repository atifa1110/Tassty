package com.example.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("restaurant_id")
    val restaurantId: String,

    @SerializedName("address_id")
    val addressId: String,

    @SerializedName("total_price")
    val totalPrice: Int,

    @SerializedName("delivery_fee")
    val deliveryFee: Int,

    val discount: Int,

    @SerializedName("total_order")
    val totalOrder: Int,

    @SerializedName("items")
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    @SerializedName("menu_id")
    val menuId: String,
    val quantity: Int,
    val price: Int,
    val options: String,
    val notes: String
)
