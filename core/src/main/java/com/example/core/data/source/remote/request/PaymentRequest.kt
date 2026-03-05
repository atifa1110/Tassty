package com.example.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("payment_method")
    val paymentMethod: String
)
