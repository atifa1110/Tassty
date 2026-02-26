package com.example.tassty

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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


object CardTransformations {
    class CardNumber : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
            var out = ""
            for (i in trimmed.indices) {
                out += trimmed[i]
                if (i % 4 == 3 && i != 15) out += "-"
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 3 -> offset
                        offset <= 7 -> offset + 1
                        offset <= 11 -> offset + 2
                        else -> offset + 3
                    }
                }
                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 4 -> offset
                        offset <= 9 -> offset - 1
                        offset <= 14 -> offset - 2
                        else -> offset - 3
                    }
                }
            }
            return TransformedText(AnnotatedString(out), offsetMapping)
        }
    }

    // Untuk Expiry Date (MM/YY)
    class ExpiryDateTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
            var out = ""

            for (i in trimmed.indices) {
                out += trimmed[i]
                // Tambah slash HANYA setelah karakter ke-2 (index 1)
                // DAN jika masih ada karakter setelahnya
                if (i == 1 && trimmed.length > 2) out += "/"
            }

            val offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // Jika sudah melewati angka ke-2, posisi geser +1 karena ada slash
                    if (offset <= 2) return offset
                    return offset + 1
                }

                override fun transformedToOriginal(offset: Int): Int {
                    // Jika di tampilan (transformed) sudah melewati posisi ke-3 (angka + slash)
                    if (offset <= 2) return offset
                    return offset - 1
                }
            }

            return TransformedText(AnnotatedString(out), offsetMapping)
        }
    }
}