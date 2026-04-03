package com.example.core.data.mapper

import com.example.core.data.model.OrderDto
import com.example.core.data.model.OrderItemDto
import com.example.core.ui.utils.DateFormatter
import com.example.core.domain.model.Order
import com.example.core.domain.model.OrderItem
import com.example.core.ui.model.OrderStatus

fun OrderDto.toDomain(): Order{
    return Order(
        id= this.id,
        orderNumber = this.orderNumber,
        restaurantName = this.restaurantName,
        restaurantImage = this.restaurantImage,
        status = when (this.status.uppercase()) {
            "PENDING_PAYMENT" -> OrderStatus.PENDING
            "PLACED" -> OrderStatus.PLACED
            "PREPARING" -> OrderStatus.PREPARING
            "ON_DELIVERY" -> OrderStatus.ON_DELIVERY
            "COMPLETED" -> OrderStatus.COMPLETED
            "CANCELLED" -> OrderStatus.CANCELLED
            else -> OrderStatus.PENDING
        },
        finalAmount = this.finalAmount,
        queueNumber = this.queueNumber,
        createdAt = DateFormatter.utcToLocalDateTime(this.createdAt)
    )
}

fun OrderItemDto.toDomain(): OrderItem{
    return OrderItem(
        id = this.id,
        menuReviewId = this.menuReviewId?:"",
        menuName = this.menuName,
        price = this.price,
        quantity = this.quantity,
        imageUrl = this.imageUrl,
        options = this.options?:"",
        notes = if (!notes.isNullOrBlank()) "Notes : $notes" else ""
    )
}