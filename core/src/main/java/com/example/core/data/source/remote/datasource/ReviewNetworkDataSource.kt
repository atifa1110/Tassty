package com.example.core.data.source.remote.datasource

import com.example.core.data.model.RestaurantReviewDto
import com.example.core.data.model.ReviewDto
import com.example.core.data.source.remote.api.ReviewApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.ReviewMenuRequest
import com.example.core.data.source.remote.request.ReviewRestaurantRequest
import javax.inject.Inject

class ReviewNetworkDataSource @Inject constructor(
    private val service: ReviewApiService
){
    suspend fun createReviewRestaurant(orderId: String, request: ReviewRestaurantRequest): TasstyResponse<Unit> {
        return safeApiCall { service.createRestaurantReview(orderId = orderId, request = request) }
    }

    suspend fun createReviewMenu(orderItemId: String, request: ReviewMenuRequest): TasstyResponse<Unit> {
        return safeApiCall { service.createMenuReview(orderItemId = orderItemId, request = request) }
    }
    suspend fun getReview(restId: String): TasstyResponse<List<ReviewDto>> {
        return safeApiCall { service.getReview(restId = restId) }
    }

    suspend fun getReviewDetail(restId: String): TasstyResponse<RestaurantReviewDto> {
        return safeApiCall { service.getReviewDetail(restId = restId) }
    }
}