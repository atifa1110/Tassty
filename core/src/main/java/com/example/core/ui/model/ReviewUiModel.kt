package com.example.core.ui.model

import com.example.core.domain.model.RatingSummary

data class RestaurantReviewUiModel(
    val summary: RatingSummary,
    val reviews: List<ReviewUiModel>
)

class ReviewUiModel (
    val id: String,
    val username: String,
    val profileImage: String,
    val rating: Int,
    val comment: String,
    val orderItems: String,
    val date: String,
)