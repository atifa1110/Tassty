package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.SearchRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchRestaurantUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>> {
         return repository.getSearchRestaurants(filter)
    }
}
