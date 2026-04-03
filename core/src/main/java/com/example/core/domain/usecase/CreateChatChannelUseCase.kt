package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateChatChannelUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(orderId: String): Flow<TasstyResponse<String>>{
        return repository.createChatChannel(orderId)
    }
}