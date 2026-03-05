package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProcessOrderPaymentStripeUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(orderId: String, paymentMethod: String): Flow<TasstyResponse<String>>{
        return repository.paymentStripe(orderId,paymentMethod)
    }
}