package com.example.core.domain.repository

import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.data.source.local.database.model.MenuWithRestaurant
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Collection
import com.example.core.domain.model.CollectionRestaurantWithMenu
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    suspend fun addCollection(title: String)
    suspend fun updateCollectionName(collectionId: String,name: String)
    suspend fun initializeSystemCollections()
    fun getCollections() : Flow<List<Collection>>
    suspend fun getCollectionIdsByMenu(menuId: String): List<String>

    fun getMenuCollectionById(collectionId: String) : Flow<List<CollectionRestaurantWithMenu>>

    fun observeFavoriteMenuIds(): Flow<Set<String>>
    fun observeIsMenuFavorite(menuId: String): Flow<Boolean>

    suspend fun addWishlist(
        menu: MenuEntity,
        restaurant: RestaurantEntity,
        collectionIds: List<String>
    )

    suspend fun removeWishlist(
        menuId: String,
        collectionIds: List<String>
    )

    suspend fun deleteCollectionById(collectionId: String)
}