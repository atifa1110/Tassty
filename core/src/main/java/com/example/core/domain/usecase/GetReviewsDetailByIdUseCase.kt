package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.RestaurantReview
import com.example.core.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsDetailByIdUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    operator fun invoke(restaurantId: String): Flow<TasstyResponse<RestaurantReview>> {
        return repository.getReviewDetail(restaurantId)
    }
}