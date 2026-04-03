package com.example.core.domain.model

import com.example.core.ui.model.OrderStatus
import org.threeten.bp.LocalDateTime


data class Order(
    val id: String,
    val orderNumber: String,
    val restaurantName: String,
    val restaurantImage: String,
    val status: OrderStatus,
    val finalAmount: Int,
    val queueNumber: Int,
    val createdAt: LocalDateTime
)
