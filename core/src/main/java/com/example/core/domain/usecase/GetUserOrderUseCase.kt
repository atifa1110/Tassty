package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Order
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke() : Flow<TasstyResponse<List<Order>>>{
        return repository.getUserOrder()
    }
}