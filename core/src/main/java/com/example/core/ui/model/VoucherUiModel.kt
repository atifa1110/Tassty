package com.example.core.ui.model

import com.example.core.domain.model.Voucher
import com.example.core.domain.utils.toDisplayFormat

data class VoucherUiModel(
    val voucher: Voucher,
    val isUsable: Boolean,
    val isSelected: Boolean = false,
){
    val expireLabel : String
        get() = voucher.expiryDate.toDisplayFormat()

    val startLabel : String
        get() = voucher.startDate.toDisplayFormat()
}
