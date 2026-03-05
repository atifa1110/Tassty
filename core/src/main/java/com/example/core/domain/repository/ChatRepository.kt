package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
   suspend fun connectStreamUser(
        userId: String,
        userName: String,
        image: String,
        token: String?
    ): Flow<TasstyResponse<String>>
}