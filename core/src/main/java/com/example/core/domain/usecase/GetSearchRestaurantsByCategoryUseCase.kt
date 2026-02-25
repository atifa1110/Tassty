package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchRestaurantsByCategoryUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) {
    operator fun invoke(id: String,filter: RestaurantSearchFilter) : Flow<TasstyResponse<List<RestaurantWithMenu>>> = flow{
        val result = restaurantRepository.getSearchRestaurantsByCategory(id,filter)

        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.data, result.meta))
                }

                is TasstyResponse.Error -> emit(result) // just pass error downstream
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}