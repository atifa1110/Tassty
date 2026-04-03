package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.model.OrderDto
import com.example.core.data.source.remote.datasource.OrderNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.CreateChannelRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.data.source.remote.request.PaymentRequest
import com.example.core.domain.model.DetailOrder
import com.example.core.domain.model.Order
import com.example.core.domain.model.PaymentChannel
import com.example.core.domain.repository.OrderRepository
import com.example.core.ui.model.OrderStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import javax.inject.Inject
import kotlin.collections.map

class OrderRepositoryImpl @Inject constructor(
    private val dataSource: OrderNetworkDataSource
) : OrderRepository{

    private val refreshSignal = MutableSharedFlow<String>(extraBufferCapacity = 1)

    override fun triggerOrderUpdate(orderId: String) {
        refreshSignal.tryEmit(orderId)
    }

    override fun getPaymentChannel(): Flow<TasstyResponse<List<PaymentChannel>>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.getPaymentChannel()
        when(result){
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(result.data?.map { it.toDomain() }, result.meta))
            }
            is TasstyResponse.Error -> {
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun createOrder(request: OrderRequest): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.createOrder(request)
        when(result){
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(result.data?.orderId, result.meta))
            }
            is TasstyResponse.Error -> {
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserOrder(): Flow<TasstyResponse<List<Order>>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.getUserOrder()
        when(result){
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(result.data?.map { it.toDomain() }, result.meta))
            }
            is TasstyResponse.Error -> {
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun paymentStripe(
        orderId: String,
        paymentMethod: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())
        val request = PaymentRequest(paymentMethod)
        val result = dataSource.paymentStripe(orderId, request)
        when(result){
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(result.data?.receiptUrl, result.meta))
            }
            is TasstyResponse.Error -> {
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailOrder(orderId: String): Flow<TasstyResponse<DetailOrder>> = flow {
        emit(TasstyResponse.Loading())

        val result = dataSource.getDetailOrder(orderId)
        when(result){
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(result.data?.toDomain(), result.meta))
            }
            is TasstyResponse.Error -> {
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getOrderSummary(orderId: String): Flow<TasstyResponse<Order>> =
        refreshSignal
            // Filter: Cuma dengerin notif yang ID-nya cocok
            .filter { id -> id == orderId }
            // Trik Sakti: Tambahin satu sinyal "palsu" di awal buat pemicu pertama
            .onStart { emit(orderId) }
            // 3. Transformasi: Setiap ada sinyal (awal/notif), panggil API
            .transform { id ->
                emit(TasstyResponse.Loading())
                val response = dataSource.getOrderSummary(id)
                val result = handleResponse(response)
                emit(result)

                // Kalau sudah selesai, kita nggak butuh dengerin lagi
                if ((result as? TasstyResponse.Success)?.data?.status == OrderStatus.COMPLETED) {
                    return@transform
                }
            }
            .distinctUntilChanged { old, new ->
                val oldStatus = (old as? TasstyResponse.Success)?.data?.status
                val newStatus = (new as? TasstyResponse.Success)?.data?.status
                if (old is TasstyResponse.Success && new is TasstyResponse.Success) {
                    oldStatus == newStatus
                } else false
            }
            .flowOn(Dispatchers.IO)

    override fun createChatChannel(orderId: String): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading())

        val request = CreateChannelRequest(orderId)
        val result = dataSource.createChatChannel(request)
        when(result){
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(result.data?.channel, result.meta))
            }
            is TasstyResponse.Error -> {
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)
}

private fun handleResponse(response: TasstyResponse<OrderDto>): TasstyResponse<Order> {
    return when (response) {
        is TasstyResponse.Success -> {
            TasstyResponse.Success(response.data?.toDomain(), response.meta)
        }
        is TasstyResponse.Error -> {
            TasstyResponse.Error(response.meta)
        }
        is TasstyResponse.Loading -> {
            TasstyResponse.Loading()
        }
    }
}