package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.OrderItemRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(
        restaurantId: String,
        voucherId: String,
        addressId: String,
        totalOrder: Int,
        items: List<OrderItemRequest>
    ): Flow<TasstyResponse<String>>{
        val request = OrderRequest(
            restaurantId = restaurantId,
            voucherId = voucherId,
            addressId = addressId,
            totalOrder = totalOrder,
            items = items
        )
        return repository.createOrder(request)
    }
}