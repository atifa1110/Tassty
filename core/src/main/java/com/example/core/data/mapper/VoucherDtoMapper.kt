package com.example.core.data.mapper

import com.example.core.data.model.VoucherDto
import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherScope
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.model.VoucherType
import org.threeten.bp.LocalDate

fun VoucherDto.toDomain() : Voucher{
    return Voucher(
        id = this.id,
        code = this.code,
        title = this.title,
        imageUrl = "https://assets.example.com/icons/paypal.png",
        description = this.description,
        type = VoucherType.valueOf(this.type),
        discountType = DiscountType.valueOf(this.discountType),
        scope = VoucherScope.valueOf(this.scope),
        discountValue = this.discountValue,
        maxDiscount = this.maxDiscount,
        minOrderValue = this.minOrderValue,
        minOrderLabel = this.minOrderLabel,
        startDate = LocalDate.parse(this.startDate ?: "1970-01-01"),
        expiryDate = LocalDate.parse(this.expiryDate ?: "1970-01-01"),
        status = VoucherStatus.valueOf(this.status)
    )
}