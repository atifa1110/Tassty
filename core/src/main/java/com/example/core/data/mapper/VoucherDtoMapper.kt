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
        id = id,
        code = code,
        imageUrl = imageUrl,
        title = title,
        description = description,
        type = VoucherType.valueOf(type.uppercase()),
        discountType = DiscountType.valueOf(discountType.uppercase()),
        scope = VoucherScope.valueOf(scope.uppercase()),
        discountValue = discountValue,
        maxDiscount = maxDiscount,
        minOrderValue = minOrderValue,
        minOrderLabel = minOrderLabel,
        startDate = LocalDate.parse(startDate),
        expiryDate = LocalDate.parse(expiryDate),
        terms = terms,
        status = VoucherStatus.valueOf(status.uppercase())
    )
}