package com.example.core.data.repository

import com.example.core.data.source.remote.datasource.UserNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.UserSetUpRequest
import com.example.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userNetworkDataSource: UserNetworkDataSource
) : UserRepository{

    override suspend fun addUserAddress(request: UserSetUpRequest): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading)

        val response = userNetworkDataSource.addUserAddress(request)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> emit(TasstyResponse.Success(response.meta.message,response.meta))
            else -> {}
        }
    }
}