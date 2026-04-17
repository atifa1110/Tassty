package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.Favorite
import com.example.core.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeIsFavorite(id: String): Flow<Boolean>
    fun observeFavoriteIds(): Flow<Set<String>>
    suspend fun addFavorite(restaurant: DetailRestaurant)
    suspend fun removeFavorite(id: String)

    fun getFavorites() : Flow<List<Favorite>>
}