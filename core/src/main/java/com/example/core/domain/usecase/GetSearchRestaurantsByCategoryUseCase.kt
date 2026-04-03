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
    operator fun invoke(id: String,filter: RestaurantSearchFilter) : Flow<TasstyResponse<List<RestaurantWithMenu>>> {
        return restaurantRepository.getSearchRestaurantsByCategory(id,filter)
    }
}