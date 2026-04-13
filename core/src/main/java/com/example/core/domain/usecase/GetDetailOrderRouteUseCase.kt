package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Route
import com.example.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailOrderRouteUseCase @Inject constructor(
    private val repository: OrderRepository
){
    operator fun invoke(orderId:String) : Flow<TasstyResponse<Route>>{
        return repository.getDetailRoute(orderId)
    }
}