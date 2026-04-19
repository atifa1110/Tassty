package com.example.tassty

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.VoucherType
import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.ui.theme.Blue200
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green200
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink500
import com.example.tassty.ui.theme.LocalCustomColors
import org.threeten.bp.LocalTime

data class VerifyArgs(
    val type: VerificationType,
    val expiresIn: Int,
    val resendDelay: Int
)

enum class VerificationType(
    @StringRes val titleRes: Int,
    @StringRes val instructionRes: Int,
    @StringRes val recoveryInfoRes: Int
) {
    REGISTRATION(
        R.string.verify_email_title,
        R.string.verify_email_instruction,
        R.string.verify_email_recovery
    ),
    FORGOT_PASSWORD(
        R.string.reset_password_title,
        R.string.reset_password_instruction,
        R.string.reset_password_recovery
    )
}

enum class RatingType(val title: String) {
    MENU("Rating Food"),
    RESTAURANT("Rating Restaurant"),
    DRIVER("Rating Driver")
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
    selectedMenus: List<CartItemUiModel>,
    deliveryCost: Int,
    voucher: VoucherUiModel?
): CartSummary {
    val subtotal = selectedMenus.sumOf { it.price * it.quantity }
    val deliveryFee = if (subtotal > 0) deliveryCost else 0
    val voucherResult = calculateVoucherResult(
        subtotal = subtotal,
        deliveryFee = deliveryFee,
        voucher = voucher
    )

    val totalDiscount = voucherResult.discountAmount + voucherResult.shippingDiscount
    val totalOrder = subtotal + (deliveryFee - voucherResult.shippingDiscount) - voucherResult.discountAmount

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

fun getStaticMapUrl(
    centerLat: Double,
    centerLng: Double,
    showMarker: Boolean = true
): String {
    val coords = "$centerLat,$centerLng"
    val baseUrl = "https://maps.googleapis.com/maps/api/staticmap?" +
            "center=$coords&zoom=15&size=400x400&scale=2"

    return if (showMarker) {
        "$baseUrl&markers=color:red%7C$coords&key=${BuildConfig.MAPS_API_KEY}"
    } else {
        "$baseUrl&key=${BuildConfig.MAPS_API_KEY}"
    }
}

@Composable
fun getPaymentIcon(iconKey: String): Int {
    return when (iconKey) {
        "BCA" -> R.drawable.bca
        "BRI" -> R.drawable.bri
        "BNI" -> R.drawable.bni
        "MANDIRI" -> R.drawable.mandiri
        "PERMATA" -> R.drawable.permata
        "ALFAMART" -> R.drawable.alfamart
        "INDOMARET" -> R.drawable.indomaret
        "OVO" -> R.drawable.ewallet_ovo
        "DANA" -> R.drawable.dana
        "LINKAJA" -> R.drawable.linkaja
        "QRIS" -> R.drawable.qris
        else -> R.drawable.credit_card
    }
}

data class StatusUIConfig(
    val title: String,
    val description: String,
    val icon: Int,
    val iconColor: Color,
    val strokeColor: Color,
    val containerColor: Color,
    val stepIndex: Int
)

@Composable
fun OrderStatus.getUIConfig(): StatusUIConfig {
    return when (this) {
        OrderStatus.PLACED -> StatusUIConfig(
            title = "Your order have been placed",
            description = "The restaurant will make your order in a moment",
            icon = R.drawable.clipboard_check,
            iconColor = Pink500,
            strokeColor = Pink200,
            containerColor = LocalCustomColors.current.pink,
            stepIndex = 0
        )
        OrderStatus.PREPARING -> StatusUIConfig(
            title = "Preparing your order",
            description = "The restaurant is now cooking your order",
            icon = R.drawable.cooking,
            iconColor = Orange500,
            strokeColor = Orange200,
            containerColor = LocalCustomColors.current.orange,
            stepIndex = 1
        )
        OrderStatus.ON_DELIVERY -> StatusUIConfig(
            title = "Your food is coming",
            description = "Driver is on the way to you",
            icon = R.drawable.delivery,
            iconColor = Blue500,
            strokeColor = Blue200,
            containerColor = LocalCustomColors.current.blue,
            stepIndex = 2
        )
        OrderStatus.COMPLETED -> StatusUIConfig(
            title = "Your food is delivered",
            description = "Enjoy your meal",
            icon = R.drawable.thumb_up,
            iconColor = Green500,
            strokeColor = Green200,
            containerColor = LocalCustomColors.current.green,
            stepIndex = 3
        )
        OrderStatus.CANCELLED -> StatusUIConfig(
            title = "Order Cancelled",
            description = "Your order has been cancelled",
            icon = R.drawable.x,
            iconColor = Green500,
            strokeColor = Green200,
            containerColor = LocalCustomColors.current.green,
            stepIndex = -1
        )
        else -> StatusUIConfig(
            title = "Order Cancelled",
            description = "Your order has been cancelled",
            icon = R.drawable.x,
            iconColor = Green500,
            strokeColor = Green200,
            containerColor = LocalCustomColors.current.green,
            stepIndex = -1
        )
    }
}
enum class ChatTab(val title: String) {
    CHAT("Chats"),
    NOTIFICATION("Notification")
}

private fun getGreetingMessage(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 0..11 -> "Good Morning! 👋"
        in 12..15 -> "Good Afternoon! ☀️"
        in 16..18 -> "Good Evening! 🌆"
        else -> "Good Night! 🌙"
    }
}

@Composable
fun getFilterIconRes(filter: FilterOptionUi<OrderFilterCategory>): Int {
    return when (filter.key) {
        "all" -> R.drawable.collection
        "ongoing" -> R.drawable.delivery
        "completed" -> R.drawable.category
        else -> R.drawable.flag
    }
}