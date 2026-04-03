package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.ReviewNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.ReviewMenuRequest
import com.example.core.data.source.remote.request.ReviewRestaurantRequest
import com.example.core.domain.model.RestaurantReview
import com.example.core.domain.model.Review
import com.example.core.domain.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val dataSource: ReviewNetworkDataSource
) : ReviewRepository{

    override fun createReviewRestaurant(
        orderId: String,
        restaurantId: String,
        rating: Int,
        comment: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val request = ReviewRestaurantRequest(restaurantId,rating,comment)
        val response = dataSource.createReviewRestaurant(orderId,request)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.meta.message,response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun createReviewMenu(
        orderId: String,
        orderItemId: String,
        rating: Int,
        tags: String,
        comment: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val request = ReviewMenuRequest(orderItemId,rating,tags,comment)
        val response = dataSource.createReviewMenu(orderId,request)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.meta.message,response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getReview(restaurantId: String): Flow<TasstyResponse<List<Review>>> = flow {
        emit(TasstyResponse.Loading())

        val response = dataSource.getReview(restaurantId)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.map { it.toDomain()},response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getReviewDetail(restaurantId: String): Flow<TasstyResponse<RestaurantReview>> = flow {
        emit(TasstyResponse.Loading())

        val response = dataSource.getReviewDetail(restaurantId)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(),response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

}