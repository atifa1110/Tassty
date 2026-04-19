package com.example.tassty.util

import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherScope
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.model.VoucherType
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.VoucherUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.threeten.bp.LocalDate

object VoucherData {
    val voucherDomain = listOf(
        Voucher(
            id = "VOU-001",
            code = "Voucher1",
            imageUrl = "https://assets.example.com/icons/paypal.png",
            title = "Diskon 20% Semua Restoran",
            description = "Use with GoPay or 7 other options, \nmax Rp15K discount.",
            type = VoucherType.DISCOUNT,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.GLOBAL,
            discountValue = 20,
            maxDiscount = 30000,
            minOrderValue = 75000,
            minOrderLabel = "Min. order Rp75.000",
            startDate = LocalDate.of(2025,6,10),
            expiryDate = LocalDate.of(2025,10,10),
            status = VoucherStatus.AVAILABLE,
        ),
        Voucher(
            id = "VOU-002",
            code = "Vocuher1",
            imageUrl = "https://assets.example.com/icons/restaurant_exclusive.png",
            title = "Chef's Special 15% Off",
            description = "Valid only at 'The Spice Garden' \nrestaurant.",
            type = VoucherType.DISCOUNT,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.RESTAURANT,
            discountValue = 15,
            maxDiscount = 10000,
            minOrderValue = 40000,
            minOrderLabel = "Min. order Rp40.000",
            startDate = LocalDate.of(2025,6,10),
            expiryDate = LocalDate.of(2025,10,10),
            status = VoucherStatus.AVAILABLE,
        ),
        Voucher(
            id = "VOU-003",
            code = "Voucher1",
            imageUrl = "https://assets.example.com/icons/paypal.png",
            title = "Diskon 20% Semua Restoran",
            description = "Use with GoPay or 7 other options, \nmax Rp15K discount.",
            type = VoucherType.DISCOUNT,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.GLOBAL,
            discountValue = 20,
            maxDiscount = 30000,
            minOrderValue = 75000,
            minOrderLabel = "Min. order Rp75.000",
            startDate = LocalDate.of(2025,6,10),
            expiryDate = LocalDate.of(2025,10,10),
            status = VoucherStatus.AVAILABLE
        ),
    )

    val voucherUiModel: ImmutableList<VoucherUiModel>
        get() = voucherDomain.map { it.toUiModel() }.toImmutableList()
}