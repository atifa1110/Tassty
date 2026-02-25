package com.example.core.data.source.remote.api

import com.example.core.data.model.ReviewDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewApiService {

    @GET("reviews/{id}")
    suspend fun getReviewRestaurant(
        @Path("id") id: String
    ): BaseResponse<List<ReviewDto>>

}