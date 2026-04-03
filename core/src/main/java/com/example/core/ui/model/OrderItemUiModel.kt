package com.example.core.ui.model

data class OrderItemUiModel(
    val id: String,
    val menuReviewId: String,
    val quantity: String,
    val menuName: String,
    val imageUrl: String,
    val notesSummary: String
)