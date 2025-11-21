package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.data.source.local.database.entity.MenuCollectionEntity
import androidx.room.OnConflictStrategy
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.database.entity.CollectionWithMenus
import com.example.core.domain.model.CollectionListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert
    suspend fun insertCollection(collection: MenuCollectionEntity)

    // Menginsert item menu ke dalam koleksi (junction table)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMenuIntoCollection(crossRefs: List<CollectionMenuCrossRef>)

    @Query("SELECT EXISTS(SELECT 1 FROM collection_menu_cross_ref WHERE serverId = :menuId LIMIT 1)")
    suspend fun isMenuInAnyCollection(menuId: String): Boolean

    @Query("SELECT collectionId FROM collection_menu_cross_ref WHERE serverId = :menuId")
    suspend fun getCollectionIdsForMenu(menuId: String): List<Int>

    // Mengambil semua koleksi dengan count dan thumbnail item pertama
    @Query("""
        SELECT 
            c.collectionId, 
            c.name,
            COUNT(cm.serverId) AS menuCount, 
            (
                SELECT cm_sub.imageUrl 
                FROM collection_menu_cross_ref cm_sub 
                WHERE cm_sub.collectionId = c.collectionId
                LIMIT 1
            ) AS firstItemImageUrl
        FROM collections c
        LEFT JOIN collection_menu_cross_ref cm ON c.collectionId = cm.collectionId
        GROUP BY c.collectionId, c.name
    """)
    fun getAllCollectionViews(): Flow<List<CollectionListItem>>

    @Transaction
    @Query("SELECT * FROM collections WHERE collectionId = :id")
    fun getCollectionWithMenus(id: Int): Flow<CollectionWithMenus>

    @Query("DELETE FROM collection_menu_cross_ref WHERE serverId = :menuId")
    suspend fun deleteMenuFromAllCollections(menuId: String)
}