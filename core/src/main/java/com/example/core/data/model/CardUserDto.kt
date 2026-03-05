package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class CardUserDto (
    @SerializedName("id")
    val id: String = "",

    @SerializedName("stripe_pm_id")
    val stripeId: String = "",

    @SerializedName("cardholder_name")
    val cardholderName: String = "",

    @SerializedName("masked_number")
    val maskedNumber: String = "",

    @SerializedName("card_brand")
    val cardBrand: String = "",

    @SerializedName("exp_month")
    val expMonth: Int = 0,

    @SerializedName("exp_year")
    val expYear: Int = 0,

    @SerializedName("theme_color")
    val themeColor: String = "",

    @SerializedName("theme_background")
    val themeBackground: String = "",

    @SerializedName("status")
    val status: String = "",
)