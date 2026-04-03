package com.example.core.data.source.remote.datasource

import com.example.core.data.model.ChatChannelResponse
import com.example.core.data.model.DetailOrderDto
import com.example.core.data.model.OrderData
import com.example.core.data.model.OrderDto
import com.example.core.data.model.PaymentChannelDto
import com.example.core.data.model.PaymentDto
import com.example.core.data.source.remote.api.OrderApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.data.source.remote.request.CreateChannelRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.data.source.remote.request.PaymentRequest
import javax.inject.Inject

class OrderNetworkDataSource @Inject constructor(
    private val apiService: OrderApiService
) {
    suspend fun getPaymentChannel() : TasstyResponse<List<PaymentChannelDto>>{
        return safeApiCall { apiService.getPaymentChannel() }
    }

    suspend fun createOrder(request: OrderRequest) : TasstyResponse<OrderData>{
        return safeApiCall { apiService.createOrder(request) }
    }

    suspend fun getUserOrder() : TasstyResponse<List<OrderDto>>{
        return safeApiCall { apiService.getUserOrders() }
    }

    suspend fun paymentStripe(orderId: String, request: PaymentRequest) : TasstyResponse<PaymentDto>{
        return safeApiCall { apiService.paymentStripe(orderId,request)}
    }

    suspend fun getDetailOrder(orderId: String) : TasstyResponse<DetailOrderDto>{
        return safeApiCall { apiService.getDetailOrder(orderId) }
    }

    suspend fun getOrderSummary(orderId: String) : TasstyResponse<OrderDto>{
        return safeApiCall { apiService.getOrderSummary(orderId) }
    }

    suspend fun createChatChannel(request: CreateChannelRequest) : TasstyResponse<ChatChannelResponse>{
        return safeApiCall { apiService.createChatChannel(request) }
    }
}