package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.UserSetUpRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addUserAddress(request: UserSetUpRequest): Flow<TasstyResponse<String>>
}