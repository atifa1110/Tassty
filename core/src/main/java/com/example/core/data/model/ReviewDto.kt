package com.example.core.data.model
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    val id: String,

    @SerializedName("order_id")
    val orderId: String,

    @SerializedName("restaurant_id")
    val restaurantId: String,

    @SerializedName("user_name")
    val username: String,

    @SerializedName("profile_image")
    val profileImage: String,

    val rating: Int,
    val comment: String,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("order_items")
    val orderItems: String
)
