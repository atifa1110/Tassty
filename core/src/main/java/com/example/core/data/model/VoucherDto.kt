package com.example.core.data.model

data class VoucherDto(
    val id: String,
    val code: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val type: String,
    val discountType: String,
    val scope: String,
    val discountValue: Int,
    val maxDiscount: Int,
    val minOrderValue: Int,
    val minOrderLabel: String,
    val startDate: String,
    val expiryDate: String,
    val terms: String,
    val status: String
)


