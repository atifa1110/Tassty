package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Category
import com.example.core.domain.model.RestaurantWithMenu
import com.example.core.domain.utils.RestaurantSearchFilter
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<TasstyResponse<List<Category>>>

    fun getSearchRestaurantsByCategory(id: String,filter: RestaurantSearchFilter): Flow<TasstyResponse<List<RestaurantWithMenu>>>
}