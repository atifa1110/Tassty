package com.example.core.domain.repository

import com.example.core.data.model.PaymentDto
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.OrderItemRequest
import com.example.core.data.source.remote.request.PaymentRequest
import com.example.core.domain.model.DetailOrder
import com.example.core.domain.model.Order
import com.example.core.domain.model.PaymentChannel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getPaymentChannel() : Flow<TasstyResponse<List<PaymentChannel>>>

    fun createOrder(restaurantId: String,addressId: String,totalPrice: Int,
                    deliveryFee: Int,discount: Int,totalOrder: Int,items: List<OrderItemRequest>
    ): Flow<TasstyResponse<String>>

    fun getUserOrder() : Flow<TasstyResponse<List<Order>>>

    fun paymentStripe(orderId: String, paymentMethod: String
    ): Flow<TasstyResponse<String>>

    fun getDetailOrder(orderId: String) : Flow<TasstyResponse<DetailOrder>>
}