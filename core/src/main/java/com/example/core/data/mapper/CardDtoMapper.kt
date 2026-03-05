package com.example.core.data.mapper

import com.example.core.data.model.CardUserDto
import com.example.core.domain.model.CardUser

fun CardUserDto.toDomain(): CardUser {
    return CardUser(
        id = this.id,
        stripeId = this.stripeId,
        cardholderName = this.cardholderName,
        maskedNumber = this.maskedNumber,
        expYear = this.expYear,
        expMonth = this.expMonth,
        cardBrand = this.cardBrand,
        themeColor = this.themeColor,
        themeBackground = this.themeBackground,
        status = this.status
    )
}