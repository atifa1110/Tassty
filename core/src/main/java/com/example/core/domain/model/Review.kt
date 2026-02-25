package com.example.core.domain.model

import org.threeten.bp.LocalDate

data class Review(
    val id: String,
    val username: String,
    val profileImage: String,
    val rating: Int,
    val comment: String,
    val orderItems: String,
    val date: LocalDate,
)
