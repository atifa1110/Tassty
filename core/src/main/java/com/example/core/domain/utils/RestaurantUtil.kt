package com.example.core.domain.utils

import com.example.core.domain.model.LocationDetails
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import com.example.core.ui.model.MenuStatus
import com.example.core.ui.model.RestaurantStatus
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.sqrt

fun Restaurant.getTodayStatus(): RestaurantStatus {
    val today = LocalDate.now().dayOfWeek.name // e.g. "MONDAY"
    val todayInfo = operationalHours.find {
        it.day.equals(today, ignoreCase = true)
    } ?: return RestaurantStatus.OFFDAY

    if (todayInfo.hours.equals("CLOSED", ignoreCase = true))
        return RestaurantStatus.CLOSED

    val (openStr, closeStr) = todayInfo.hours.split("-").map { it.trim() }
    val now = LocalTime.now()
    val open = LocalTime.parse(openStr)
    val close = LocalTime.parse(closeStr)

    return if (now.isAfter(open) && now.isBefore(close)) {
        RestaurantStatus.OPEN
    } else {
        RestaurantStatus.CLOSED
    }
}

fun Menu.getTodayStatus(): RestaurantStatus {
    val today = LocalDate.now().dayOfWeek.name // e.g. "MONDAY"
    val todayInfo = operationalHours.find {
        it.day.equals(today, ignoreCase = true)
    } ?: return RestaurantStatus.OFFDAY

    if (todayInfo.hours.equals("CLOSED", ignoreCase = true))
        return RestaurantStatus.CLOSED

    val (openStr, closeStr) = todayInfo.hours.split("-").map { it.trim() }
    val now = LocalTime.now()
    val open = LocalTime.parse(openStr)
    val close = LocalTime.parse(closeStr)

    return if (now.isAfter(open) && now.isBefore(close)) {
        RestaurantStatus.OPEN
    } else {
        RestaurantStatus.CLOSED
    }
}

fun Menu.getStatus(restaurantStatus: RestaurantStatus) : MenuStatus{
    return when {
        restaurantStatus != RestaurantStatus.OPEN -> MenuStatus.CLOSED
        isAvailable && (maxOrderQuantity ?: 1) > 0 -> MenuStatus.AVAILABLE
        else -> MenuStatus.SOLDOUT
    }
}

fun calculateHaversine(loc1: LocationDetails, loc2: LocationDetails): Int {
    // Logika riilnya lebih kompleks, ini hanya simulasi
    val latDiff = (loc1.latitude - loc2.latitude) * 111000 // Konversi sederhana ke meter
    val lonDiff = (loc1.longitude - loc2.longitude) * 111000
    return (sqrt(latDiff * latDiff + lonDiff * lonDiff) / 1.5).toInt() // Dibagi 1.5 untuk perkiraan jarak
}

fun Int.toCleanRupiahFormat(): String {
    val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()

    val formatRupiah = NumberFormat.getCurrencyInstance(localeID).apply {
        // Erase coma and 0 behind
        maximumFractionDigits = 0
    }

    // format into currency
    return formatRupiah.format(this.toLong())
}
