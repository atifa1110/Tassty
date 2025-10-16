package com.example.core.ui.mapper

import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import com.example.core.ui.model.VoucherUiModel
import org.threeten.bp.LocalDate

fun Voucher.toUiModel(): VoucherUiModel {
    val now = LocalDate.now()
    val isUsable = status == VoucherStatus.AVAILABLE &&
            now.isAfter(startDate.minusDays(1)) &&
            now.isBefore(expiryDate.plusDays(1))

    return VoucherUiModel(
        voucher = this,
        isUsable = isUsable,
        isSelected = false
    )
}
