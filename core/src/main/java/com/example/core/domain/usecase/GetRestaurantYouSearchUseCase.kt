package com.example.core.domain.usecase

import com.example.core.data.mapper.dummyRestaurant
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRestaurantYouSearchUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke() : Flow<TasstyResponse<List<Restaurant>>> = flow {
        val result = restaurantRepository.getNearbyRestaurants()

        result.collect { result ->
            when(result){
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.data?.take(7), result.meta))
                }

                is TasstyResponse.Error -> emit(result)
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}