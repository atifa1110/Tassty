package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.DetailNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailMenu
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val dataSource: DetailNetworkDataSource
) : DetailRepository{

    override fun getDetailRestaurant(id: String): Flow<TasstyResponse<DetailRestaurant>> = flow {
        emit(TasstyResponse.Loading)

        val response = dataSource.getDetailRestaurant(id)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailMenu(id: String): Flow<TasstyResponse<DetailMenu>> = flow {
        emit(TasstyResponse.Loading)

        val response = dataSource.getDetailMenu(id)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)
}