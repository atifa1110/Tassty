package com.example.core.ui.model

data class CardUserUiModel(
    val id: String,
    val stripeId: String,
    val maskedNumber: String,
    val cardholderName: String,
    val cardBrand: String,
    val expDate: String,
    val themeColor: String,
    val themeBackground: String,
    val isActive: Boolean,
    val isSelected : Boolean,
    val isSwipeActionVisible: Boolean
)
