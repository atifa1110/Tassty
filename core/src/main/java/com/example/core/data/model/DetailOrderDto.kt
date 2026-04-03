package com.example.core.data.model

import com.google.gson.annotations.SerializedName

data class DetailOrderDto(
    val id: String,
    @SerializedName("order_number")
    val orderNumber: String,
    @SerializedName("user_id")
    val userId: String,
    val status: String,
    @SerializedName("total_price")
    val totalPrice: Int,
    @SerializedName("delivery_fee")
    val deliveryFee: Int,
    val discount: Int,
    @SerializedName("final_amount")
    val finalAmount: Int,
    @SerializedName("payment_status")
    val paymentStatus: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("queue_number")
    val queueNumber: Int,
    val driver: DriverDto,
    @SerializedName("chat_channel_id")
    val chatChannelId : String? = null,
    @SerializedName("restaurant_review_id")
    val restaurantReviewId: String?= null,
    val restaurant: RestaurantDto,
    @SerializedName("user_addresses")
    val userAddress: UserAddressDto,
    @SerializedName("order_items")
    val orderItems: List<OrderItemDto>,
    @SerializedName("user_payment_methods")
    val cardPayment: CardUserDto
)