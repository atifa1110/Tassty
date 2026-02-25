package com.example.core.ui.mapper

import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherScope
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.model.VoucherType
import com.example.core.ui.model.VoucherUiModel
import org.threeten.bp.LocalDate
import kotlin.String

fun Voucher.toUiModel(): VoucherUiModel {
    val now = LocalDate.now()
    val isUsable = status == VoucherStatus.AVAILABLE &&
            now.isAfter(startDate.minusDays(1)) &&
            now.isBefore(expiryDate.plusDays(1))

    return VoucherUiModel(
        id = this.id,
        code=this.code,
        title=this.title,
        imageUrl=this.imageUrl,
        description=this.description,
        type=this.type,
        discountType=this.discountType,
        scope=this.scope,
        discountValue=this.discountValue,
        maxDiscount=this.maxDiscount,
        minOrderValue=this.minOrderValue,
        minOrderLabel=this.minOrderLabel,
        startDate=this.startDate,
        expiryDate=this.expiryDate,
        status= this.status,
        isUsable = isUsable,
        isSelected = false
    )
}
