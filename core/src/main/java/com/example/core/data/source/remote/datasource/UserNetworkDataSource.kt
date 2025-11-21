package com.example.core.data.source.remote.datasource

import com.example.core.data.source.remote.api.UserApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall2
import com.example.core.data.source.remote.request.UserSetUpRequest
import javax.inject.Inject

class UserNetworkDataSource@Inject constructor(
    private val userApiService: UserApiService
) {

    suspend fun addUserAddress(request: UserSetUpRequest): TasstyResponse<Unit> {
        return safeApiCall2 {
            userApiService.addUserAddress(request)
        }
    }
}