package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class OrderDto (
    @SerializedName("id")
    val id: String = "",

    @SerializedName("order_number")
    val orderNumber: String = "",

    @SerializedName("restaurant_name")
    val restaurantName: String = "",

    @SerializedName("restaurant_image")
    val restaurantImage: String = "",

    @SerializedName("status")
    val status: String = "",

    @SerializedName("final_amount")
    val finalAmount: Int = 0,

    @SerializedName("queue_number")
    val queueNumber: Int = 0,

    @SerializedName("created_at")
    val createdAt: String = ""
)