package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class SetupDto(
    val clientSecret: String
)

data class OrderData(
    @SerializedName("order_id")
    val orderId: String
)