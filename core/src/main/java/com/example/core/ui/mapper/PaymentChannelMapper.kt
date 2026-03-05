package com.example.core.ui.mapper

import com.example.core.domain.model.PaymentCategory
import com.example.core.domain.model.PaymentChannel
import com.example.core.ui.model.PaymentChannelUiModel

fun PaymentChannel.toUiModel(): PaymentChannelUiModel {
    return PaymentChannelUiModel(
        businessId = this.businessId,
        channelCode = this.channelCode,
        name = this.name,
        channelCategory = when (this.channelCategory) {
            PaymentCategory.VIRTUAL_ACCOUNT -> "Virtual Account"
            PaymentCategory.RETAIL_OUTLET -> "Convenience Store"
            PaymentCategory.EWALLET -> "E-Wallet"
            PaymentCategory.QRIS -> "QR Code"
            PaymentCategory.UNKNOWN -> "Lainnya"
        },
        iconKey = this.channelCode,
        isEnabled = this.isEnabled,
        isSelected = false
    )
}