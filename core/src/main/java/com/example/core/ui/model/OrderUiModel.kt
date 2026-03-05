package com.example.core.ui.model

import android.graphics.Color
import org.threeten.bp.LocalDate

data class OrderUiModel(
    val id: String,
    val orderNumber: String,
    val restaurantName: String,
    val restaurantImage: String,
    val status: OrderStatus = OrderStatus.PENDING,
    val finalAmount: String,
    val queueNumber: String,
    val orderDate : LocalDate,
    val displayHeader:String,
    val displayTime:String
)

enum class OrderStatus {
    PENDING,
    PLACED,
    PREPARING,
    ON_DELIVERY,
    COMPLETED,
    CANCELLED
}

