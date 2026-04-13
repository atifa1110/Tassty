package com.example.core.data.source.remote.datasource

import com.example.core.data.model.CategoryDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.api.CategoryApiService
import com.example.core.data.source.remote.api.CategoryLocationApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.domain.utils.RestaurantSearchFilter
import javax.inject.Inject

class CategoryNetworkDataSource @Inject constructor(
    private val service: CategoryApiService,
    private val locationService: CategoryLocationApiService
){
    suspend fun getAllCategories(): TasstyResponse<List<CategoryDto>> {
        return safeApiCall {
            service.getAllCategories()
        }
    }

    suspend fun searchRestaurantsByCategory(
        categoryId: String,
        filter: RestaurantSearchFilter
    ): TasstyResponse<List<RestaurantMenuDto>> {
        return safeApiCall {
            locationService.searchRestaurantsByCategory(
                categoryId = categoryId,
                keyword = filter.keyword,
                minRating = filter.minRating,
                priceRange = filter.priceRange,
                mode = filter.mode,
                cuisineId = filter.cuisineId,
                sorting = filter.sorting
            )
        }
    }
}