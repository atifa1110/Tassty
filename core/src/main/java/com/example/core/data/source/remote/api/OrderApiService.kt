package com.example.core.data.source.remote.api

import com.example.core.data.model.ChatChannelResponse
import com.example.core.data.model.DetailOrderDto
import com.example.core.data.model.OrderData
import com.example.core.data.model.OrderDto
import com.example.core.data.model.PaymentChannelDto
import com.example.core.data.model.PaymentDto
import com.example.core.data.source.remote.network.BaseResponse
import com.example.core.data.source.remote.request.CreateChannelRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.data.source.remote.request.PaymentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @GET("orders/payment_channels")
    suspend fun getPaymentChannel(
    ): BaseResponse<List<PaymentChannelDto>>

    @POST("orders")
    suspend fun createOrder(
        @Body request: OrderRequest
    ): BaseResponse<OrderData>

    @GET("orders/list")
    suspend fun getUserOrders(
    ): BaseResponse<List<OrderDto>>

    @POST("orders/{orderId}/payments/stripe")
    suspend fun paymentStripe(
        @Path("orderId") orderId: String,
        @Body request: PaymentRequest
    ): BaseResponse<PaymentDto>

    @GET("orders/{orderId}")
    suspend fun getDetailOrder(
        @Path("orderId") orderId: String,
    ): BaseResponse<DetailOrderDto>

    @GET("orders/{orderId}/summary")
    suspend fun getOrderSummary(
        @Path("orderId") orderId: String,
    ): BaseResponse<OrderDto>

    @POST("chats/channel")
    suspend fun createChatChannel(
        @Body request: CreateChannelRequest
    ): BaseResponse<ChatChannelResponse>

}