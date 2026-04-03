package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateReviewRestaurantUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    operator fun invoke(
        orderId: String,
        restaurantId: String,
        rating: Int,
        comment: String
    ): Flow<TasstyResponse<String>>{
        return repository.createReviewRestaurant(orderId,restaurantId,rating,comment)
    }
}