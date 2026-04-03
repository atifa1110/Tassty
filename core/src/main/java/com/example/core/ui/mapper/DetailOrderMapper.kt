package com.example.core.ui.mapper

import com.example.core.domain.model.DetailOrder
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.utils.DateFormatter
import java.util.Locale

fun DetailOrder.toUiModel(): DetailOrderUiModel {
    return DetailOrderUiModel(
        id = this.id,
        userId = this.userId,
        orderNumber = this.orderNumber,
        status = when (this.status.uppercase()) {
            "PENDING_PAYMENT" -> OrderStatus.PENDING
            "PLACED" -> OrderStatus.PLACED
            "PREPARING" -> OrderStatus.PREPARING
            "ON_DELIVERY" -> OrderStatus.ON_DELIVERY
            "COMPLETED" -> OrderStatus.COMPLETED
            "CANCELLED" -> OrderStatus.CANCELLED
            else -> OrderStatus.PENDING
        },
        totalPrice = this.totalPrice,
        deliveryFee = this.deliveryFee,
        discount = this.discount,
        finalAmount = this.finalAmount,
        paymentStatus = this.paymentStatus,
        createdAt = DateFormatter.formatOrderDate(this.createdAt),
        driver = this.driver.toUiModel(),
        chatChannelId = this.chatChannelId,
        restaurantReviewId = this.restaurantReviewId,
        restaurant = this.restaurant.toUiModel(),
        userAddress = this.userAddress.toUiModel(),
        orderItems = this.orderItems.map { it.toUiModel() },
        queueNumber = String.format(Locale.US,"#%03d", this.queueNumber),
        cardPayment = this.cardPayment.toUiModel()
    )
}