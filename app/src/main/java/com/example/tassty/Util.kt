package com.example.tassty

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import java.net.URLEncoder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.VoucherType
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.ui.theme.Blue200
import com.example.tassty.ui.theme.Blue50
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Green200
import com.example.tassty.ui.theme.Green50
import com.example.tassty.ui.theme.Green500
import com.example.tassty.ui.theme.Orange200
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink200
import com.example.tassty.ui.theme.Pink50
import com.example.tassty.ui.theme.Pink500
import com.google.android.gms.maps.model.BitmapDescriptor
import androidx.core.graphics.createBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.CircleCropTransformation
import com.example.tassty.component.LocationItem
import com.example.tassty.ui.theme.LocalCustomColors
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.URL

data class VerifyArgs(
    val type: VerificationType,
    val expiresIn: Int,
    val resendDelay: Int
)

enum class VerificationType(val title: String, val instruction: String, val recoveryInfo: String) {
    REGISTRATION("Verify Your Email.",
        "Please enter the 6-digit activation code sent to: ", "to activate your account."),
    FORGOT_PASSWORD("Reset Your Password.",
        "Please enter the security code sent to: ","to recover your account.")
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


// Helper function (bisa taruh di file terpisah atau di dalam ViewModel/Composable)
suspend fun loadCircularBitmapFromUrl(
    context: Context,
    imageUrl: String,
    sizeDp: Dp = 40.dp // Sesuaikan ukuran marker
): BitmapDescriptor? {
    val loader = ImageLoader(context)
    val sizePx = with(Density(context)) { sizeDp.toPx() }.toInt()

    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .allowHardware(false) // WAJIB false biar bisa di-crop
        .transformations(CircleCropTransformation()) // Otomatis jadi bulat dari Coil
        .size(sizePx)
        .build()

    return try {
        // 1. Download Gambar
        val result = (loader.execute(request) as? SuccessResult)?.drawable
        val bitmap = (result as? BitmapDrawable)?.bitmap

        // 2. Ubah Bitmap jadi BitmapDescriptor (format yang diminta Google Maps)
        if (bitmap != null) {
            BitmapDescriptorFactory.fromBitmap(bitmap)
        } else {
            // Kalau gagal, kasih marker default/placeholder
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        // Kalau error (internet mati/url salah), kasih marker default
        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    }
}

/**
 * Helper untuk mengambil daftar titik yang sudah dilewati driver
 */
fun calculatePathTraveled(fullPath: List<LatLng>, currentPos: LatLng): List<LatLng> {
    // Cari index titik terdekat, jangan pakai indexOf biasa karena rentan beda presisi
    val index = fullPath.indexOfFirst {
        it.latitude == currentPos.latitude && it.longitude == currentPos.longitude
    }

    return if (index != -1) {
        fullPath.subList(0, index + 1)
    } else {
        // Kalau nggak ketemu, balikin minimal titik awal biar nggak kosong banget
        if (fullPath.isNotEmpty()) listOf(fullPath.first(), currentPos) else emptyList()
    }
}

suspend fun getDirections(origin: LatLng, dest: LatLng): List<LatLng> {
    return withContext(Dispatchers.IO) {
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&key=${BuildConfig.MAPS_API_KEY}"

        try {
            val response = URL(url).readText()
            val json = JSONObject(response)

            // 1. Cek status dulu sebelum ambil 'routes'
            val status = json.getString("status")
            if (status != "OK") {
                println("DEBUG_MAP_ERROR: Status dari Google = $status")
                if (json.has("error_message")) {
                    println("DEBUG_MAP_ERROR: Pesan = ${json.getString("error_message")}")
                }
                return@withContext emptyList() // Langsung keluar kalau gak OK
            }

            val routes = json.getJSONArray("routes")
            if (routes.length() > 0) {
                val encodedPoints = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")

                decodePolyline(encodedPoints)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("DEBUG_MAP_EXCEPTION: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        poly.add(LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5))
    }
    return poly
}