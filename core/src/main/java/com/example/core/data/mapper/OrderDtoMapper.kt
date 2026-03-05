package com.example.core.data.mapper

import com.example.core.data.model.OrderDto
import com.example.core.data.model.OrderItemDto
import com.example.core.domain.model.Order
import com.example.core.domain.model.OrderItem
import org.threeten.bp.LocalDateTime

fun OrderDto.toDomain(): Order{
    return Order(
        id= this.id,
        restaurantName = this.restaurantName,
        restaurantImage = this.restaurantImage,
        status = this.status,
        finalAmount = this.finalAmount,
        queueNumber = this.queueNumber,
        createdAt = LocalDateTime.parse(this.createdAt)
    )
}

fun OrderItemDto.toDomain(): OrderItem{
    return OrderItem(
        id = this.id,
        menuName = this.menuName,
        price = this.price,
        quantity = this.quantity,
        imageUrl = this.imageUrl,
        options = this.options?:"",
        notes = if (!notes.isNullOrBlank()) "Notes : $notes" else ""
    )
}