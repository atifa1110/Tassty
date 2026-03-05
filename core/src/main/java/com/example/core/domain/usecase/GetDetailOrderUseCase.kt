package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailOrder
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(orderId: String) :Flow<TasstyResponse<DetailOrder>> {
        return repository.getDetailOrder(orderId)
    }
}