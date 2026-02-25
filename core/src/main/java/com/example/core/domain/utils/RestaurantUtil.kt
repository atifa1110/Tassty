package com.example.core.domain.utils

import com.example.core.domain.model.Voucher
import com.example.core.domain.model.VoucherStatus
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.Locale

fun Int.toCleanRupiahFormat(): String {
    val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()

    val formatRupiah = NumberFormat.getCurrencyInstance(localeID).apply {
        maximumFractionDigits = 0
    }

    // format into currency
    return formatRupiah.format(this.toLong())
}


fun LocalDate.toDisplayFormat(
    locale: Locale = Locale.forLanguageTag("id-ID")
): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", locale)
    return this.format(formatter)
}


data class RestaurantSearchFilter(
    val keyword: String? = null,
    val minRating: String? = null,
    val priceRange: String? = null,
    val mode: String? = null,
    val cuisineId: String? = null,
    val sorting: String? = null
)


