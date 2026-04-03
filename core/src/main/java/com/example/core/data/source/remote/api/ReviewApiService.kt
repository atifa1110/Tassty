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

    @GET("reviews/{id}")
    suspend fun getReview(
        @Path("id") id: String
    ): BaseResponse<List<ReviewDto>>

    @GET("reviews/{id}/detail")
    suspend fun getReviewDetail(
        @Path("id") id: String
    ): BaseResponse<RestaurantReviewDto>

    @POST("reviews/{id}/restaurant")
    suspend fun createRestaurantReview(
        @Path("orderId") orderId: String,
        @Body request: ReviewRestaurantRequest
    ): BaseResponse<Unit>

    @POST("reviews/{id}/menu")
    suspend fun createMenuReview(
        @Path("orderId") orderId: String,
        @Body request: ReviewMenuRequest
    ): BaseResponse<Unit>

}