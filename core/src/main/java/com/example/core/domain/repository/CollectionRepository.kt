package com.example.core.domain.repository

import com.example.core.data.source.local.database.entity.CollectionWithMenus
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.CollectionListItem
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun getCollectionIdsForMenu(menuId: String): List<Int>
    fun getCollectionListForView(): Flow<List<CollectionListItem>>

    suspend fun createNewCollectionOnly(collectionName: String): ResultWrapper<String>

    suspend fun saveMenuToCollections(
        menuId: String,
        selectedCollectionIds: List<Int>
    ): ResultWrapper<String>

    fun getCollectionDetail(collectionId: Int): Flow<CollectionWithMenus>

    suspend fun checkMenuWishlistStatus(menuId: String): Boolean
}