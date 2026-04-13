package com.example.core.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("stripe_pm_id")
    val stripePmId: String
)
