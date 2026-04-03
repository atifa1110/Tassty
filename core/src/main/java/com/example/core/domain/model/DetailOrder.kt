package com.example.core.domain.model

import org.threeten.bp.LocalDateTime

data class DetailOrder(
    val id: String,
    val orderNumber: String,
    val userId: String,
    val status: String,
    val totalPrice: Int,
    val deliveryFee: Int,
    val discount: Int,
    val finalAmount: Int,
    val paymentStatus: String,
    val createdAt: LocalDateTime,
    val queueNumber: Int,
    val driver: Driver,
    val chatChannelId: String,
    val restaurantReviewId: String,
    val restaurant: Restaurant,
    val userAddress: UserAddress,
    val orderItems: List<OrderItem>,
    val cardPayment: CardUser
)