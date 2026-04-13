package com.example.core.domain.usecase

import com.example.core.data.model.RouteUpdatePayload
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackDriverLocationUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(orderId: String): Flow<RouteUpdatePayload> =
        repository.trackDriverLocation(orderId)
}