package com.example.core.domain.model

import org.threeten.bp.LocalDate

data class Voucher (
    val id: String,
    val code: String,
    val imageUrl: String,
    val title: String,
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
    val terms: String,
    val status: VoucherStatus
)

enum class VoucherType(val displayName: String) {
    DISCOUNT("Discount"),
    SHIPPING("Shipping"),
    CASHBACK("Cashback")
}
enum class DiscountType(val displayName: String) {
    PERCENTAGE("Percentage"),
    FIXED("Fixed"),
}
enum class VoucherScope {
    GLOBAL,
    RESTAURANT
}

enum class VoucherStatus { AVAILABLE, USED, EXPIRED, UPCOMING, OUT_OF_STOCK, CLOSED }