package com.example.core.data.mapper

import com.example.core.data.model.ReviewDto
import com.example.core.domain.model.Review
import org.threeten.bp.LocalDate

fun ReviewDto.toDomain() = Review(
    id = this.id,
    username = this.username,
    profileImage = this.profileImage,
    rating = this.rating,
    comment = this.comment,
    date = this.createdAt?.substring(0, 10).let { LocalDate.parse(it) },
    orderItems = this.orderItems
)
