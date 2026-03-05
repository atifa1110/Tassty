package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.OrderItemRequest
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(
        restaurantId: String,addressId: String,totalPrice: Int,
        deliveryFee: Int,discount: Int,totalOrder: Int,items: List<OrderItemRequest>
    ): Flow<TasstyResponse<String>>{
        return repository.createOrder(restaurantId,addressId,totalPrice,deliveryFee,discount,totalOrder,items)
    }
}