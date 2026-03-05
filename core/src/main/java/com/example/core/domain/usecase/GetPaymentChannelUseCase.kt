package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.PaymentChannel
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentChannelUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke() : Flow<TasstyResponse<List<PaymentChannel>>>{
        return repository.getPaymentChannel()
    }
}