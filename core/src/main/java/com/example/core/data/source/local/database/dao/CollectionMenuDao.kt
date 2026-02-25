package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionMenuDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ref: CollectionMenuCrossRef)

    @Query("""
        DELETE FROM collection_menu
        WHERE collectionId = :collectionId
        AND menuId = :menuId
    """)
    suspend fun deleteByCollectionMenuId(collectionId: String, menuId: String)

    @Query("""
        DELETE FROM collection_menu
        WHERE collectionId = :collectionId
    """)
    suspend fun deleteByCollectionId(collectionId: String)

    @Query("""
        SELECT m.imageUrl
        FROM menus m
        INNER JOIN collection_menu c
        ON m.id = c.menuId
        WHERE c.collectionId = :collectionId
        LIMIT 1
    """)
    suspend fun getAnyMenuImage(collectionId: String): String?

    @Query("""
        SELECT collectionId
        FROM collection_menu
        WHERE menuId = :menuId
    """)
    fun getCollectionIdsByMenuId(menuId: String): List<String>

    /**
     * Ambil SEMUA menuId
     * yang masih punya relasi ke collection mana pun
     */
    @Query("""
        SELECT DISTINCT menuId
        FROM collection_menu
    """)
    fun observeFavoriteMenuIds(): Flow<List<String>>

    @Query("""
    SELECT EXISTS(
        SELECT 1
        FROM collection_menu
        WHERE menuId = :menuId
    )
""")
    fun observeIsMenuFavorite(menuId: String): Flow<Boolean>
}
