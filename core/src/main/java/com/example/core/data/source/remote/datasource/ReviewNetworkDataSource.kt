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
    suspend fun getReview(restaurantId: String): TasstyResponse<List<ReviewDto>> {
        return safeApiCall { service.getReview(restaurantId) }
    }

    suspend fun getReviewDetail(restaurantId: String): TasstyResponse<RestaurantReviewDto> {
        return safeApiCall { service.getReviewDetail(restaurantId) }
    }

    suspend fun createReviewRestaurant(orderId: String, request: ReviewRestaurantRequest): TasstyResponse<Unit> {
        return safeApiCall { service.createRestaurantReview(orderId,request) }
    }

    suspend fun createReviewMenu(orderId: String, request: ReviewMenuRequest): TasstyResponse<Unit> {
        return safeApiCall { service.createMenuReview(orderId,request) }
    }
}