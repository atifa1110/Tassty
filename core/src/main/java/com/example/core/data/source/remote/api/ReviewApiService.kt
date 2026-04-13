package com.example.core.data.source.remote.api

import com.example.core.data.model.RestaurantReviewDto
import com.example.core.data.model.ReviewDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.ReviewMenuRequest
import com.example.core.data.source.remote.request.ReviewRestaurantRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApiService {

    @POST("reviews/{orderId}/restaurant")
    suspend fun createRestaurantReview(
        @Path("orderId") orderId: String,
        @Body request: ReviewRestaurantRequest
    ): BaseResponse<Unit>

    @POST("reviews/{orderItemId}")
    suspend fun createMenuReview(
        @Path("orderItemId") orderItemId: String,
        @Body request: ReviewMenuRequest
    ): BaseResponse<Unit>

    @GET("reviews/{restId}")
    suspend fun getReview(
        @Path("restId") restId: String
    ): BaseResponse<List<ReviewDto>>

    @GET("reviews/{restId}/detail")
    suspend fun getReviewDetail(
        @Path("restId") restId: String
    ): BaseResponse<RestaurantReviewDto>
}