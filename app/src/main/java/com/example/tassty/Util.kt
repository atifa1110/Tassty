package com.example.tassty

import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.VoucherType
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.VoucherUiModel
fun hashUrl(url: String): String {
    return url.hashCode().toString()
}

fun getPickMenuSubtitle(required: Boolean, max: Int): String {
    return if (required) {
        if (max == 1) {
            "pick 1"
        } else {
            "pick up to $max"
        }
    } else {
        if (max == 1) {
            "optional"
        } else {
            "optional pick up to $max"
        }
    }
}

data class VoucherResult(
    val discountAmount: Int = 0,
    val shippingDiscount: Int = 0,
    val cashbackAmount: Int = 0
)

data class CartSummary(
    val subtotal: Int = 0,
    val deliveryFee: Int = 0,
    val discount: Int = 0,
    val totalOrder: Int = 0
)

fun calculateCartSummary(
    menus: List<CartItemUiModel>,
    deliveryCost: Int,
    voucher: VoucherUiModel?
): CartSummary {
    val selectedMenus = menus.filter { it.isSelected }
    val subtotal = selectedMenus.sumOf { it.price * it.quantity }
    val deliveryFee = if (subtotal > 0) deliveryCost else 0
    val voucherResult = calculateVoucherResult(
        subtotal = subtotal,
        deliveryFee = deliveryFee,
        voucher = voucher
    )
    val totalDiscount =
        voucherResult.discountAmount + voucherResult.shippingDiscount

    val totalOrder =
        subtotal + (deliveryFee - voucherResult.shippingDiscount) - voucherResult.discountAmount

    return CartSummary(
        subtotal = subtotal,
        deliveryFee = deliveryFee,
        discount = totalDiscount,
        totalOrder = totalOrder
    )
}

private fun calculateDiscountAmount(subtotal: Int, voucher: VoucherUiModel): Int {
    if (subtotal < voucher.minOrderValue) return 0
    val rawDiscount = when (voucher.discountType) {
        DiscountType.PERCENTAGE -> (subtotal * (voucher.discountValue / 100.0)).toInt()
        DiscountType.FIXED -> voucher.discountValue
    }
    return minOf(rawDiscount, voucher.maxDiscount)
}

private fun calculateVoucherResult(
    subtotal: Int,
    deliveryFee: Int,
    voucher: VoucherUiModel?
): VoucherResult {
    voucher ?: return VoucherResult()
    return when (voucher.type) {
        VoucherType.DISCOUNT -> {
            VoucherResult(
                discountAmount = calculateDiscountAmount(subtotal, voucher)
            )
        }
        VoucherType.SHIPPING -> {
            VoucherResult(
                shippingDiscount = calculateDiscountAmount(deliveryFee, voucher)
            )
        }
        VoucherType.CASHBACK -> {
            VoucherResult(
                cashbackAmount = calculateDiscountAmount(subtotal, voucher)
            )
        }
    }
}

fun getStaticMapUrl(lat: Double, lng: Double): String {
    val coordinates = "$lat,$lng"
    return "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=$coordinates" +
            "&zoom=15" +
            "&scale=2" +
            "&size=400x400" +
            "&markers=color:red%7C$coordinates" +
            "&key=${BuildConfig.MAPS_API_KEY}"
}
