package com.example.core.domain.model

data class CardUser(
    val id: String,
    val stripeId: String,
    val cardholderName: String,
    val maskedNumber: String,
    val cardBrand: String,
    val expMonth: Int,
    val expYear: Int,
    val themeColor: String,
    val themeBackground: String,
    val status: String,
){
    val formattedExpDate: String get() {
            val month = expMonth.toString()
            val year = expYear.toString().takeLast(2)
            return "$month/$year"
        }
}
