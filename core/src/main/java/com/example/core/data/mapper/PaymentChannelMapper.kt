package com.example.core.data.mapper

import com.example.core.data.model.PaymentChannelDto
import com.example.core.domain.model.PaymentCategory
import com.example.core.domain.model.PaymentChannel

fun PaymentChannelDto.toDomain(): PaymentChannel {
    return PaymentChannel(
        businessId = this.businessId,
        channelCode = this.channelCode,
        currency = this.currency,
        name = this.name,
        channelCategory = when (this.channelCategory) {
            "VIRTUAL_ACCOUNT" -> PaymentCategory.VIRTUAL_ACCOUNT
            "RETAIL_OUTLET" -> PaymentCategory.RETAIL_OUTLET
            "EWALLET" -> PaymentCategory.EWALLET
            "QRIS" -> PaymentCategory.QRIS
            else -> PaymentCategory.UNKNOWN
        },
        isEnabled = this.isEnabled
    )
}