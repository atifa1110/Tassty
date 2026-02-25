package com.example.core.ui.mapper

import com.example.core.domain.model.Review
import com.example.core.domain.utils.toDisplayFormat
import com.example.core.ui.model.ReviewUiModel

fun Review.toUiModel() = ReviewUiModel(
        id = this.id,
        username = username,
        profileImage = profileImage,
        rating = rating,
        comment = comment,
        date = this.date.toDisplayFormat(),
        orderItems = this.orderItems
)