package com.example.core.data.mapper

import com.example.core.data.model.DetailOrderDto
import com.example.core.domain.model.DetailOrder
import com.example.core.ui.utils.DateFormatter

fun DetailOrderDto.toDomain(): DetailOrder {
    return DetailOrder(
        id = this.id,
        userId = this.userId,
        orderNumber = this.orderNumber,
        status = this.status,
        totalPrice = this.totalPrice,
        deliveryFee = this.deliveryFee,
        discount = this.discount,
        finalAmount = this.finalAmount,
        paymentStatus = this.paymentStatus,
        createdAt = DateFormatter.utcToLocalDateTime(this.createdAt),
        driver = this.driver.toDomain(),
        chatChannelId = this.chatChannelId?:"",
        restaurantReviewId = this.restaurantReviewId?:"",
        restaurant = this.restaurant.toDomain(),
        userAddress = this.userAddress.toDomain(),
        orderItems = this.orderItems.map { it.toDomain() },
        queueNumber = this.queueNumber,
        cardPayment = this.cardPayment.toDomain()
    )
}