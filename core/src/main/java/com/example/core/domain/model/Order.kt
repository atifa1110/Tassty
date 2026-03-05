package com.example.core.domain.model

import org.threeten.bp.LocalDateTime

data class Order(
    val id: String = "",
    val orderNumber: String = "",
    val restaurantName: String = "",
    val restaurantImage: String = "",
    val status: String = "",
    val finalAmount: Int = 0,
    val queueNumber: Int = 0,
    val createdAt: LocalDateTime
)
