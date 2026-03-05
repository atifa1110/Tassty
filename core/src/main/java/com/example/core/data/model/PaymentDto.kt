package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class PaymentDto(
    @SerializedName("invoice_id")
    val invoiceId: String,
    val status: String,
    @SerializedName("receipt_url")
    val receiptUrl: String
)