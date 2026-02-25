package com.example.core.data.repository

import android.util.Log
import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.ReviewNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Review
import com.example.core.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val dataSource: ReviewNetworkDataSource
) : ReviewRepository{

    override fun getReviewRestaurant(id: String): Flow<TasstyResponse<List<Review>>> = flow {
        emit(TasstyResponse.Loading)

        val response = dataSource.getReviewRestaurant(id)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                Log.d("ReviewRepository",response.data.toString())
                emit(TasstyResponse.Success(response.data?.map { it.toDomain()},response.meta))
            }
            else -> {}
        }
    }

}