package com.example.core.data.mapper

import com.example.core.data.model.RatingSummaryDto
import com.example.core.data.model.RestaurantReviewDto
import com.example.core.data.model.ReviewDto
import com.example.core.data.model.StarDistributionDto
import com.example.core.domain.model.RatingSummary
import com.example.core.domain.model.RestaurantReview
import com.example.core.domain.model.Review
import com.example.core.domain.model.StarDistribution
import org.threeten.bp.LocalDate

fun ReviewDto.toDomain() = Review(
    id = this.id,
    username = this.username?:"Guest",
    profileImage = this.profileImage?:"Guest",
    rating = this.rating,
    comment = this.comment,
    date = this.createdAt.substring(0, 10).let { LocalDate.parse(it) },
    orderItems = this.orderItems
)

fun StarDistributionDto.toDomain() = StarDistribution(
    star =  this.star,
    count = this.count,
    percentage = this.percentage
)

fun RatingSummaryDto.toDomain() = RatingSummary(
    averageRating = this.averageRating,
    totalReviews = this.totalReviews,
    distribution = this.distribution.map { it.toDomain() }
)

fun RestaurantReviewDto.toDomain() = RestaurantReview(
    summary = this.summary.toDomain(),
    reviews = this.reviews.map { it.toDomain() }
)

