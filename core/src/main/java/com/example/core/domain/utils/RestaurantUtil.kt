package com.example.core.domain.utils

import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.Locale

fun Int.toCleanRupiahFormat(): String {
    val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()

    val formatRupiah = NumberFormat.getCurrencyInstance(localeID).apply {
        maximumFractionDigits = 0
    }

    return formatRupiah.format(this.toLong())
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

fun LocalDateTime.getHeaderTimeAndDate(): DateTimeDisplay {
    val localDateTime = this.atZone(ZoneOffset.UTC)
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime()

    val orderDate = localDateTime.toLocalDate()
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val localeID = Locale("id", "ID")

    val header = when (orderDate) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> localDateTime.format(
            DateTimeFormatter.ofPattern("dd MMM yyyy", localeID)
        )
    }

    val time = localDateTime.format(
        DateTimeFormatter.ofPattern("HH:mm", localeID)
    )

    return DateTimeDisplay(
        header = header,
        time = time,
        localDate = orderDate
    )
}

data class RestaurantSearchFilter(
    val keyword: String? = null,
    val minRating: String? = null,
    val priceRange: String? = null,
    val mode: String? = null,
    val cuisineId: String? = null,
    val sorting: String? = null
)


