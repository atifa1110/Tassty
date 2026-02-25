package com.example.core.ui.model

import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherScope
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.model.VoucherType
import com.example.core.domain.utils.toCleanRupiahFormat
import com.example.core.domain.utils.toDisplayFormat
import org.threeten.bp.LocalDate

data class VoucherUiModel(
    val id: String,
    val code: String,
    val title: String,
    val imageUrl: String,
    val description: String,
    val type: VoucherType,
    val discountType: DiscountType,
    val scope: VoucherScope,
    val discountValue: Int,
    val maxDiscount: Int,
    val minOrderValue: Int,
    val minOrderLabel: String,
    val startDate: LocalDate,
    val expiryDate: LocalDate,
    val status: VoucherStatus,
    val isUsable: Boolean,
    val isSelected: Boolean,
){
    val expireLabel : String
        get() = expiryDate.toDisplayFormat()

    val startLabel : String
        get() = startDate.toDisplayFormat()

    val formatMinOrder : String
        get() = minOrderValue.toCleanRupiahFormat()
}
