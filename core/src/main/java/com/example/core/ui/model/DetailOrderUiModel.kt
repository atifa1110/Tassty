package com.example.core.ui.model


data class DetailOrderUiModel(
    val id: String,
    val orderNumber: String,
    val userId: String,
    val status: OrderStatus,
    val totalPrice: Int,
    val deliveryFee: Int,
    val discount: Int,
    val finalAmount: Int,
    val paymentStatus: String,
    val createdAt: String,
    val queueNumber: String,
    val driver: DriverUiModel,
    val chatChannelId: String,
    val restaurantReviewId: String,
    val restaurant: RestaurantUiModel,
    val userAddress: UserAddressUiModel,
    val orderItems: List<OrderItemUiModel>,
    val cardPayment: CardUserUiModel
)