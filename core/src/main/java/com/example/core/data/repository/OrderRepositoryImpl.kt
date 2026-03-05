package com.example.core.data.repository

import android.util.Log
import com.example.core.data.mapper.toDomain
import com.example.core.data.model.OrderDto
import com.example.core.data.model.PaymentDto
import com.example.core.data.source.remote.datasource.OrderNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.request.OrderItemRequest
import com.example.core.data.source.remote.request.OrderRequest
import com.example.core.data.source.remote.request.PaymentRequest
import com.example.core.domain.model.DetailOrder
import com.example.core.domain.model.Order
import com.example.core.domain.model.PaymentChannel
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.collections.map

class OrderRepositoryImpl @Inject constructor(
    private val dataSource: OrderNetworkDataSource
) : OrderRepository{

    override fun getPaymentChannel(): Flow<TasstyResponse<List<PaymentChannel>>> = flow {
        emit(TasstyResponse.Loading)

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
    }

    override fun createOrder(
        restaurantId: String,addressId: String,totalPrice: Int,
        deliveryFee: Int,discount: Int,totalOrder: Int,items: List<OrderItemRequest>
    ): Flow<TasstyResponse<String>> = flow {
        val request = OrderRequest(restaurantId,addressId,totalPrice,deliveryFee,discount,totalOrder,items)
        emit(TasstyResponse.Loading)

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
    }

    override fun getUserOrder(): Flow<TasstyResponse<List<Order>>> = flow {
        emit(TasstyResponse.Loading)

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
    }

    override fun paymentStripe(
        orderId: String,
        paymentMethod: String
    ): Flow<TasstyResponse<String>> = flow {
        emit(TasstyResponse.Loading)
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
    }

    override fun getDetailOrder(orderId: String): Flow<TasstyResponse<DetailOrder>> = flow {
        emit(TasstyResponse.Loading)

        val result = dataSource.getDetailOrder(orderId)
        when(result){
            is TasstyResponse.Success -> {
                Log.d("OrderRepositoryImpl",result.data.toString())
                emit(TasstyResponse.Success(result.data?.toDomain(), result.meta))
            }
            is TasstyResponse.Error -> {
                Log.d("OrderRepositoryImpl",result.meta.message)
                emit(TasstyResponse.Error(result.meta))
            }
            else -> {}
        }
    }

}