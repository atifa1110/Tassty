package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Route
import com.example.core.domain.repository.DetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailRestaurantRouteUseCase @Inject constructor(
    private val repository: DetailRepository
) {
    operator fun invoke(id:String): Flow<TasstyResponse<Route>>{
        return repository.getDetailRoute(id)
    }
}