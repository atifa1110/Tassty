package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.SearchNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.FilterOptions
import com.example.core.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchNetworkDataSource: SearchNetworkDataSource
): SearchRepository{

    override fun filterOption(): Flow<TasstyResponse<FilterOptions>> = flow {
        emit(TasstyResponse.Loading())

        val result = searchNetworkDataSource.filterOption()
        when(result){
            is TasstyResponse.Success -> emit(TasstyResponse.Success(
                result.data?.toDomain(),result.meta)
            )
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)
}