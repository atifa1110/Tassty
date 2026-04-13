package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.SearchNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.FilterOptions
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.SearchRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dataSource: SearchNetworkDataSource
): SearchRepository{

    override fun getSearchRestaurants(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.searchRestaurants(filter)
        when(result){
            is TasstyResponse.Success -> emit(TasstyResponse.Success(
                result.data?.map { it.toDomain() },result.meta)
            )
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun filterOption(): Flow<TasstyResponse<FilterOptions>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.filterOption()
        when(result){
            is TasstyResponse.Success -> emit(TasstyResponse.Success(
                result.data?.toDomain(),result.meta)
            )
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)
}