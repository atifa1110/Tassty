package com.example.core.domain.model

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
    val createdAt: String,
    val queueNumber: Int,
    val driver: Driver,
    val restaurant: Restaurant,
    val userAddress: UserAddress,
    val orderItems: List<OrderItem>,
    val cardPayment: CardUser
)