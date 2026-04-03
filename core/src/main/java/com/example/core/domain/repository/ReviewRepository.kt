package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.RestaurantReview
import com.example.core.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun createReviewRestaurant(
        orderId: String,
        restaurantId: String,
        rating: Int,
        comment: String
    ): Flow<TasstyResponse<String>>
    fun createReviewMenu(
        orderId: String,
        orderItemId: String,
        rating: Int,
        tags: String,
        comment: String
    ): Flow<TasstyResponse<String>>

    fun getReview(restaurantId: String): Flow<TasstyResponse<List<Review>>>
    fun getReviewDetail(restaurantId: String): Flow<TasstyResponse<RestaurantReview>>
}