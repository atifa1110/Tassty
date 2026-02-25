package com.example.core.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.core.ui.model.RestaurantStatusResult
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

//DTO → Restaurant
//DB → Restaurant → RestaurantWithWishlist
data class Restaurant(
    val id: String,
    val name: String,
    val imageUrl: String,
    val categories: List<String>,
    val city: String,
    val fullAddress: String,
    val longitude: Double,
    val latitude: Double,
    val rank: Int,
    val rating: Double,
    val totalReviews: Int,
    val distance: Int,
    val deliveryCost: Int,
    val deliveryTime: String,
    val isOpenFromApi: Boolean,
    val closingTimeServerFromApi: String
) {
    val statusResult: RestaurantStatusResult
        get() = getRestaurantStatus(isOpenFromApi, closingTimeServerFromApi)
}


data class LocationDetail(
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val city: String,
)

/**
 * Menentukan status restoran (OPEN / CLOSED) berdasarkan data server.
 *
 * ## Alur logika
 * 1. Jika `isOpen = false`
 *    → restoran dianggap tutup walaupun jam tutup belum lewat.
 *
 * 2. Jika `closingTimeServer` null / kosong
 *    → diasumsikan restoran masih buka
 *    (karena server tidak mengirim jam tutup).
 *
 * 3. Jika device API 26+
 *    - Pakai `java.time` (Instant, ZonedDateTime)
 *    - Waktu server diasumsikan UTC
 *    - Dikonversi ke WIB (Asia/Jakarta)
 *
 * 4. Jika device < API 26
 *    - Fallback pakai `Date` + `SimpleDateFormat`
 *    - Tetap parsing sebagai UTC
 *
 * ## Catatan penting
 * - Timezone Indonesia diset ke Asia/Jakarta
 * - Jika server sudah kirim WIB, jangan convert lagi
 *
 * @param isOpen
 * Flag dari server apakah restoran aktif atau tidak.
 *
 * @param closingTimeServer
 * Jam tutup dari server dalam format ISO-8601 (UTC).
 * Contoh: 2026-01-11T16:00:00Z
 *
 * @return
 * - [RestaurantStatus.OPEN] jika sekarang < jam tutup
 * - [RestaurantStatus.CLOSED] jika sekarang >= jam tutup
 */
fun getRestaurantStatus(
    isOpen: Boolean,
    closingTimeServer: String?
): RestaurantStatusResult {
    // 1️⃣ Prioritas: Jika server bilang tutup manual
    if (!isOpen) {
        return RestaurantStatusResult(
            status = RestaurantStatus.CLOSED,
            message = "Tutup sekarang"
        )
    }

    // 2️⃣ Prioritas: Jika jam tutup tidak ada
    if (closingTimeServer.isNullOrBlank()) {
        return RestaurantStatusResult(
            status = RestaurantStatus.OPEN,
            message = "Buka"
        )
    }

    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ✅ Android 8.0+ (Modern & Akurat)
            val zoneId = ZoneId.of("Asia/Jakarta")
            val now = ZonedDateTime.now(zoneId)
            val closing = Instant.parse(closingTimeServer).atZone(zoneId)

            if (now.isBefore(closing)) {
                // Pakai formatter biar jam & menit otomatis 09:05 (bukan 9:5)
                val timeString = closing.format(DateTimeFormatter.ofPattern("HH:mm"))
                RestaurantStatusResult(
                    status = RestaurantStatus.OPEN,
                    message = "Buka sampai $timeString"
                )
            } else {
                RestaurantStatusResult(status = RestaurantStatus.CLOSED, message = "Tutup sekarang")
            }

        } else {
            // ✅ Android Legacy (Tetap Aman)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val closingDate = sdf.parse(closingTimeServer) ?: throw Exception("Parse failed")

            // Bandingkan dalam UTC
            val now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time

            if (now.before(closingDate)) {
                // Konversi tampilan ke Jakarta
                val displayFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                }
                RestaurantStatusResult(
                    status = RestaurantStatus.OPEN,
                    message = "Buka sampai ${displayFormat.format(closingDate)}"
                )
            } else {
                RestaurantStatusResult(status = RestaurantStatus.CLOSED, message = "Tutup sekarang")
            }
        }
    } catch (e: Exception) {
        // 3️⃣ Fallback: Jika data jam ngaco/error, anggap tutup demi keamanan transaksi
        RestaurantStatusResult(
            status = RestaurantStatus.CLOSED,
            message = "Tutup sekarang"
        )
    }
}
