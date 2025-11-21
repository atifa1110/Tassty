package com.example.core.data.source.local.datasource

import com.example.core.data.source.local.cache.MenuCache
import com.example.core.data.source.local.cache.RestaurantCache
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.database.entity.CollectionWithMenus
import com.example.core.data.source.local.database.entity.MenuCollectionEntity
import com.example.core.domain.model.CollectionListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MenuDatabaseLocalDataSource @Inject constructor(
    private val menuDao: MenuDao,
    private val menuCache: MenuCache,
    private val restaurantCache: RestaurantCache
) {
    suspend fun isMenuInAnyCollection(menuId: String): Boolean
    = menuDao.isMenuInAnyCollection(menuId)

    fun getCollectionListForView(): Flow<List<CollectionListItem>> {
        return menuDao.getAllCollectionViews()
    }

    suspend fun insertCollection(newCollection : MenuCollectionEntity) {
        return menuDao.insertCollection(newCollection)
    }

    suspend fun insertCollectionWithMenu(crossRefs: List<CollectionMenuCrossRef>){
        return menuDao.insertMenuIntoCollection(crossRefs)
    }

    fun getCollectionDetail(collectionId: Int): Flow<CollectionWithMenus> {
        return menuDao.getCollectionWithMenus(collectionId)
    }

    suspend fun getCollectionIdsForMenu(menuId: String): List<Int> {
        return menuDao.getCollectionIdsForMenu(menuId)
    }

    suspend fun deleteMenuFromAllCollections(menuId: String){
        return menuDao.deleteMenuFromAllCollections(menuId)
    }
}