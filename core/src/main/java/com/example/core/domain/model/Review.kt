package com.example.core.domain.model

import org.threeten.bp.LocalDate

data class RestaurantReview(
    val summary: RatingSummary,
    val reviews: List<Review>
)

data class RatingSummary(
    val averageRating: Double,
    val totalReviews: Int,
    val distribution: List<StarDistribution>
)

data class StarDistribution(
    val star: Int,
    val count: Int,
    val percentage: Int
)
data class Review(
    val id: String,
    val username: String,
    val profileImage: String,
    val rating: Int,
    val comment: String,
    val orderItems: String,
    val date: LocalDate,
)
