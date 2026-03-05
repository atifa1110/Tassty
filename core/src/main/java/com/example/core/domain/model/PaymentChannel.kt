package com.example.core.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Business logic representation of a Payment Channel.
 */
data class PaymentChannel(
    val businessId: String,
    val channelCode: String,
    val name: String,
    val currency: String,
    val channelCategory: PaymentCategory,
    val isEnabled: Boolean
)

/**
 * Enum to handle categories safely without using raw Strings.
 */
enum class PaymentCategory {
    VIRTUAL_ACCOUNT,
    RETAIL_OUTLET,
    EWALLET,
    QRIS,
    UNKNOWN
}