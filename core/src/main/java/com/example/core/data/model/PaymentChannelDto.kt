package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class PaymentChannelDto(
    @SerializedName("business_id")
    val businessId: String,

    @SerializedName("channel_code")
    val channelCode: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("currency")
    val currency: String,

    @SerializedName("channel_category")
    val channelCategory: String,

    @SerializedName("is_enabled")
    val isEnabled: Boolean
)