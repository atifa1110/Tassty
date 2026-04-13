package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.repository.CategoryRepository
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchRestaurantsByCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(id: String,filter: RestaurantSearchFilter) : Flow<TasstyResponse<List<RestaurantWithMenu>>> {
        return repository.getSearchRestaurantsByCategory(id,filter)
    }
}