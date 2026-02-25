package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {

    fun getReviewRestaurant(id: String): Flow<TasstyResponse<List<Review>>>
}