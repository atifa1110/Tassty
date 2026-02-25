package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository
) {
    operator fun invoke(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>> = flow {
        val result = repository.getSearchRestaurants(filter)

        result.collect { result ->
            when(result){
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.data, result.meta))
                }

                is TasstyResponse.Error -> emit(result) // just pass error downstream
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}
