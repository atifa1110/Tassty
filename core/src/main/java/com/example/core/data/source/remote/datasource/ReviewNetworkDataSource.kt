package com.example.core.data.source.remote.datasource

import com.example.core.data.model.ReviewDto
import com.example.core.data.source.remote.api.ReviewApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class ReviewNetworkDataSource @Inject constructor(
    private val service: ReviewApiService
){
    suspend fun getReviewRestaurant(id: String): TasstyResponse<List<ReviewDto>> {
        return safeApiCall { service.getReviewRestaurant(id) }
    }

}