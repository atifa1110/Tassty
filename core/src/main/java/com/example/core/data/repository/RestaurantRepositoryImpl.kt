package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.RestaurantNetworkDataSource
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.RestaurantRepository
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val remoteDataSource: RestaurantNetworkDataSource
) : RestaurantRepository {

    override suspend fun getRecommendedRestaurants(): ResultWrapper<List<Restaurant>> {
        val result = remoteDataSource.getRestaurants()
        return when (result) {
            is ResultWrapper.Success -> ResultWrapper.Success(
                result.data.map { it.toDomain() }, // mapping DTO → Domain
                result.meta
            )
            is ResultWrapper.Error -> result
            is ResultWrapper.Loading -> result
        }
    }

    override suspend fun getNearbyRestaurants(): ResultWrapper<List<Restaurant>> {
        val result = remoteDataSource.getRestaurants()
        return when (result) {
            is ResultWrapper.Success -> ResultWrapper.Success(
                result.data.map { it.toDomain() }, // mapping DTO → Domain
                result.meta
            )
            is ResultWrapper.Error -> result
            is ResultWrapper.Loading -> result
        }
    }
}