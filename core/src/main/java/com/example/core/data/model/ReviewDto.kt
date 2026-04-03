package com.example.core.data.model
import com.google.gson.annotations.SerializedName


data class RestaurantReviewDto(
    val summary: RatingSummaryDto,
    val reviews: List<ReviewDto>
)

data class RatingSummaryDto(
    @SerializedName("average_rating")
    val averageRating: Double,
    @SerializedName("total_reviews")
    val totalReviews: Int,
    val distribution: List<StarDistributionDto>
)

data class StarDistributionDto(
    val star: Int,
    val count: Int,
    val percentage: Int
)

data class ReviewDto(
    val id: String,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("restaurant_id")
    val restaurantId: String,
    @SerializedName("user_name")
    val username: String,
    @SerializedName("profile_image")
    val profileImage: String,
    val rating: Int,
    val comment: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("order_items")
    val orderItems: String
)