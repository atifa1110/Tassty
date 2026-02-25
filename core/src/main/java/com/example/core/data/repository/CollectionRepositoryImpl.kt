package com.example.core.data.repository

import android.util.Log
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.data.source.local.datasource.CollectionDataSource
import com.example.core.data.source.local.mapper.toDomain
import com.example.core.data.source.local.mapper.toGroupedDomain
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Collection
import com.example.core.domain.model.CollectionRestaurantWithMenu
import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val dataSource: CollectionDataSource,
): CollectionRepository {

    val SYSTEM_FAVORITE_ID = "system_favorite"

    override suspend fun addCollection(title: String) {
        val collectionId = UUID.randomUUID().toString()
        dataSource.addCollection(collectionId,title)
    }

    override suspend fun updateCollectionName(collectionId: String, name: String) {
        return dataSource.updateName(collectionId,name)
    }

    override suspend fun initializeSystemCollections() {
        val existing = dataSource.getCollections().first()

        if (existing.none { it.collection.id == SYSTEM_FAVORITE_ID }) {
            dataSource.addCollection(
                collectionId = SYSTEM_FAVORITE_ID,
                title = "Favorites",
                isSystem = true
            )
        }
    }

    override fun getCollections(): Flow<TasstyResponse<List<Collection>>> = flow {
        emit(TasstyResponse.Loading)

        try {
            dataSource.getCollections()
                .map { entities -> entities.map { it.toDomain() } }
                .collect { collections ->
                    emit(
                        TasstyResponse.Success(
                            data = collections,
                            meta = Meta(0, "", "")
                        )
                    )
                }
        } catch (e: Exception) {
            Log.d("CollectionRepository", e.message.orEmpty())
            emit(
                TasstyResponse.Error(
                    Meta(0, "", "Get collection failed")
                )
            )
        }
    }

    override suspend fun getCollectionIdsByMenu(menuId: String): List<String> {
        return dataSource.getCollectionIdsByMenu(menuId)
    }

    override fun getMenuCollectionById(
        collectionId: String
    ): Flow<List<CollectionRestaurantWithMenu>> {
        return dataSource.getMenuCollectionById(collectionId)
            .map { list ->
                list.toGroupedDomain()
            }
    }

    override fun observeFavoriteMenuIds(): Flow<Set<String>> {
        return dataSource
            .observeFavoriteMenuIds()
            .map { it.toSet() }
    }

    override fun observeIsMenuFavorite(menuId: String): Flow<Boolean> {
        return dataSource.observeIsMenuFavorite(menuId)
    }

    override suspend fun addWishlist(
        menu: MenuEntity,
        restaurant: RestaurantEntity,
        collectionIds: List<String>
    ) {
        return dataSource.addWishlist(menu,restaurant,collectionIds)
    }

    override suspend fun removeWishlist(
        menuId: String,
        collectionIds: List<String>
    ) {
        return dataSource.removeWishlist(menuId, collectionIds)
    }

    override suspend fun deleteCollectionById(collectionId: String) {
        return dataSource.deleteCollectionById(collectionId)
    }

}