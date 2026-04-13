package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.FilterOptions
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchRestaurants(filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>>

    fun filterOption(): Flow<TasstyResponse<FilterOptions>>
}