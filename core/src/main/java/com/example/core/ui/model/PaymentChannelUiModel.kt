package com.example.core.ui.model

data class PaymentChannelUiModel(
    val businessId: String,
    val channelCode: String,
    val name: String,
    val channelCategory: String,
    val iconKey: String,
    val isEnabled: Boolean,
    val isSelected: Boolean
)