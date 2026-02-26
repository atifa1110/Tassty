package com.example.core.data.source.remote.request

data class SaveCardRequest(
    val paymentMethodId: String,
    val themeColor: String,
    val themeBackground: String
)