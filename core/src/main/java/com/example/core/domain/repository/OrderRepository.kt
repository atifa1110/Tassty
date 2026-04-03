package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.OrderItemRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.domain.model.DetailOrder
import com.example.core.domain.model.Order
import com.example.core.domain.model.PaymentChannel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun triggerOrderUpdate(orderId: String)
    fun getPaymentChannel() : Flow<TasstyResponse<List<PaymentChannel>>>

    fun createOrder(request: OrderRequest): Flow<TasstyResponse<String>>

    fun getUserOrder() : Flow<TasstyResponse<List<Order>>>

    fun paymentStripe(orderId: String, paymentMethod: String): Flow<TasstyResponse<String>>

    fun getDetailOrder(orderId: String) : Flow<TasstyResponse<DetailOrder>>

    fun getOrderSummary(orderId: String) :  Flow<TasstyResponse<Order>>

    fun createChatChannel(orderId: String) : Flow<TasstyResponse<String>>
}