package com.example.core.ui.mapper

import com.example.core.domain.model.CardUser
import com.example.core.ui.model.CardUserUiModel

fun CardUser.toUiModel(isSwipeActionVisible: Boolean): CardUserUiModel {
    return CardUserUiModel(
        id = this.id,
        stripeId = this.stripeId,
        cardholderName = this.cardholderName,
        maskedNumber = this.maskedNumber,
        expDate = this.formattedExpDate,
        cardBrand = this.cardBrand,
        themeColor = this.themeColor,
        themeBackground = this.themeBackground,
        isActive = this.status == "ACTIVE",
        isSelected = false,
        isSwipeActionVisible = isSwipeActionVisible
    )
}