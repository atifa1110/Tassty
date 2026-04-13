package com.example.core.data.source.local.datasource

import androidx.room.Transaction
import com.example.core.data.source.local.database.dao.CleanupDao
import com.example.core.data.source.local.database.dao.CollectionDao
import com.example.core.data.source.local.database.dao.CollectionMenuDao
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.dao.RestaurantDao
import com.example.core.data.source.local.database.entity.CollectionEntity
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.data.source.local.database.model.CollectionWithMenu
import com.example.core.data.source.local.database.model.MenuWithRestaurant
import com.example.core.data.source.local.database.model.throwUserFriendlyDbError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionDataSource @Inject constructor(
    private val menuDao: MenuDao,
    private val restaurantDao: RestaurantDao,
    private val collectionMenuDao: CollectionMenuDao,
    private val collectionDao: CollectionDao
) {
    @Transaction
    suspend fun addCollection(collectionId: String, title: String,isSystem: Boolean = false) {
        try {
            collectionDao.insert(
                CollectionEntity(
                    id = collectionId,
                    title = title,
                    isSystem = isSystem
                )
            )
        }catch (e: Exception){
            e.throwUserFriendlyDbError()
        }
    }

    suspend fun updateName(collectionId: String,name: String){
        try {
            return collectionDao.updateName(collectionId, name)
        }catch (e: Exception) {
            e.throwUserFriendlyDbError()
        }
    }

    fun observeFavoriteMenuIds(): Flow<List<String>>{
        return collectionMenuDao.observeFavoriteMenuIds()
    }

    fun observeIsMenuFavorite(menuId: String): Flow<Boolean> {
        return collectionMenuDao.observeIsMenuFavorite(menuId)
    }

    @Transaction
    suspend fun addWishlist(
        menu: MenuEntity,
        restaurant: RestaurantEntity,
        collectionIds: List<String>
    ){
        try {
            // Ensure parent is exist
            restaurantDao.upsert(restaurant)
            menuDao.upsert(menu)

            // Insert relation
            if (collectionIds.isNotEmpty()) {
                collectionIds.forEach { collectionId ->
                    collectionMenuDao.insert(
                        CollectionMenuCrossRef(
                            collectionId = collectionId,
                            restaurantId = restaurant.id,
                            menuId = menu.id
                        )
                    )
                    // set image only if image url in collection is empty
                    collectionDao.updateImageIfEmpty(
                        collectionId = collectionId,
                        imageUrl = menu.imageUrl
                    )
                }
            }
        }catch (e: Exception){
            e.throwUserFriendlyDbError()
        }
    }

    @Transaction
    suspend fun removeWishlist(
        menuId: String,
        collectionIds: List<String>
    ) {
        try {
            collectionIds.forEach { collectionId ->
                collectionMenuDao.deleteByCollectionMenuId(collectionId, menuId)
                val image = collectionMenuDao.getAnyMenuImage(collectionId)
                collectionDao.updateImage(collectionId, image)
            }
        }catch (e: Exception){
            e.throwUserFriendlyDbError()
        }
    }

    @Transaction
    suspend fun deleteCollectionById(collectionId: String){
        try {
            collectionMenuDao.deleteByCollectionId(collectionId)
            collectionDao.deleteById(collectionId)
        }catch (e: Exception){
            e.throwUserFriendlyDbError()
        }
    }

    fun getCollections() : Flow<List<CollectionWithMenu>>{
        return collectionDao.getCollectionsWithMenuCount()
    }

    suspend fun getCollectionIdsByMenu(menuId: String): List<String>{
        return collectionMenuDao.getCollectionIdsByMenuId(menuId)
    }

    fun getMenuCollectionById(collectionId: String) : Flow<List<MenuWithRestaurant>>{
        return collectionMenuDao.getFavoriteMenus(collectionId)
    }
}