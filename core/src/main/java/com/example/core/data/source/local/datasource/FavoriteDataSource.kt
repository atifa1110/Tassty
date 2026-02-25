package com.example.core.data.source.local.datasource

import com.example.core.data.source.local.database.dao.FavoriteRestaurantDao
import com.example.core.data.source.local.database.entity.FavoriteRestaurantEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteDataSource @Inject constructor(
    private val dao: FavoriteRestaurantDao
) {
    fun observeIsFavorite(id: String): Flow<Boolean> =
        dao.observeIsFavorite(id)

    fun observeFavoriteIds(): Flow<Set<String>> =
        dao.observeFavoriteIds().map { it.toSet() }

    suspend fun addFavorite(restaurant: FavoriteRestaurantEntity) {
        dao.insert(restaurant)
    }

    suspend fun deleteFavorite(restaurantId: String) {
        dao.deleteById(restaurantId)
    }

    fun getFavorites(): Flow<List<FavoriteRestaurantEntity>>{
        return dao.getFavorites()
    }

}