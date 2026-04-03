package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CreateReviewMenuUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    operator fun invoke(
        orderId: String,
        orderItemId: String,
        rating: Int,
        tags: String,
        comment: String
    ): Flow<TasstyResponse<String>> {
        if (rating < 1) {
            return flowOf(TasstyResponse.Error(Meta(code = 404, status = "error", message = "Rating minimal 1 bintang ya!")))
        }

        if (tags.isBlank()) {
            return flowOf(TasstyResponse.Error(Meta(code = 404, status = "error", message ="Pilih minimal satu tag untuk review ini.")))
        }

        return repository.createReviewMenu(orderId, orderItemId, rating, tags, comment)
    }
}