package com.example.core.data.repository

import android.util.Log
import com.example.core.data.source.local.database.entity.FavoriteRestaurantEntity
import com.example.core.data.source.local.datasource.FavoriteDataSource
import com.example.core.data.source.local.mapper.toDatabase
import com.example.core.data.source.local.mapper.toDomain
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.Favorite
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dataSource: FavoriteDataSource
): FavoriteRepository {

    override fun observeIsFavorite(id: String): Flow<Boolean> =
        dataSource.observeIsFavorite(id)

    override fun observeFavoriteIds(): Flow<Set<String>> =
        dataSource.observeFavoriteIds()

    override suspend fun addFavorite(restaurant: DetailRestaurant) {
        dataSource.addFavorite(restaurant.toDatabase())
    }
    override suspend fun removeFavorite(id: String) {
        dataSource.deleteFavorite(id)
    }

    override fun getFavorites(): Flow<TasstyResponse<List<Favorite>>> =
        dataSource.getFavorites()
            .map<List<FavoriteRestaurantEntity>, TasstyResponse<List<Favorite>>> { entities ->
                TasstyResponse.Success(
                    data = entities.map { it.toDomain() },
                    meta = Meta(0, "", "")
                )
            }
            .onStart {
                emit(TasstyResponse.Loading())
            }
            .catch { e ->
                Log.d("CollectionRepository", e.message.orEmpty())
                emit(
                    TasstyResponse.Error(
                        Meta(0, "", "Get favorite failed")
                    )
                )
            }

}