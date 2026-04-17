package com.example.core.domain.utils

import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun Int.toCleanRupiahFormat(): String {
    val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()

    val formatRupiah = NumberFormat.getCurrencyInstance(localeID).apply {
        maximumFractionDigits = 0
    }

    return formatRupiah.format(this.toLong())
}

fun getSubtitle(required: Boolean, max: Int): String {
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

fun LocalDate.toDisplayFormat(
    locale: Locale = Locale.forLanguageTag("id-ID")
): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)
    return this.format(formatter)
}

data class DateTimeDisplay(
    val header: String,
    val time: String,
    val localDate: LocalDate
)

data class RestaurantSearchFilter(
    val keyword: String? = null,
    val minRating: String? = null,
    val priceRange: String? = null,
    val mode: String? = null,
    val cuisineId: String? = null,
    val sorting: String? = null
)

fun calculateDistance(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Double {
    val r = 6371000.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}