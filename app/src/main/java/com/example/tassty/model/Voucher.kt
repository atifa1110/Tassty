package com.example.tassty.model

import org.threeten.bp.LocalDate

/**
 * Data class to represent a single Voucher/Promotion entry.
 */
data class Voucher(
    val id: String,
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
    val expiryDate: LocalDate,
    // Usage Status in the UI
    val isAvailable: Boolean, // Whether the voucher can be used
    val isSelected: Boolean = false, // Checkbox/selection status (as shown in the image)
    val restaurantIds: List<String> = emptyList()
)

data class VoucherInfo(
    val about: List<String>,
    val howToUse: List<String>
)

/**
 * Enumeration defining the main usage categories for a voucher.
 * This helps differentiate usage logic (e.g., Discount vs. Shipping vs Cashback).
 */
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