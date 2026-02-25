package com.example.core.data.source.remote.datasource

import com.example.core.data.model.FilterOptionsDto
import com.example.core.data.model.RestaurantMenuDto
import com.example.core.data.source.remote.api.SearchApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import com.example.core.domain.utils.RestaurantSearchFilter
import javax.inject.Inject

class SearchNetworkDataSource @Inject constructor(
    private val searchApiService: SearchApiService
) {
    suspend fun filterOption(): TasstyResponse<FilterOptionsDto> {
        return safeApiCall{
           searchApiService.getFilterOption()
        }
    }

    suspend fun searchRestaurantsByCategory(
        categoryId: String,
        filter: RestaurantSearchFilter
    ): TasstyResponse<List<RestaurantMenuDto>> {
        return safeApiCall {
            searchApiService.searchRestaurantsByCategory(
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

    suspend fun searchRestaurants(
        filter: RestaurantSearchFilter
    ): TasstyResponse<List<RestaurantMenuDto>> {
        return safeApiCall {
            searchApiService.searchRestaurants(
                keyword = filter.keyword?:"",
                minRating = filter.minRating,
                priceRange = filter.priceRange,
                mode = filter.mode,
                cuisineId = filter.cuisineId,
                sorting = filter.sorting
            )
        }
    }
}
