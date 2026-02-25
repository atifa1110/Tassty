package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class VoucherDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("discount_type")
    val discountType: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("discount_value")
    val discountValue: Int,
    @SerializedName("max_discount")
    val maxDiscount: Int,
    @SerializedName("min_order_value")
    val minOrderValue: Int,
    @SerializedName("min_order_label")
    val minOrderLabel: String,
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("expiry_date")
    val expiryDate: String? = null,
    @SerializedName("status")
    val status: String
)


