package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.source.local.database.entity.CollectionEntity
import com.example.core.data.source.local.database.model.CollectionWithMenu
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(collection: CollectionEntity)

    @Query("DELETE FROM collections WHERE id = :collectionId")
    suspend fun deleteById(collectionId: String)

    @Query("""
        UPDATE collections
        SET imageUrl = :imageUrl
        WHERE id = :collectionId
        AND imageUrl IS NULL
    """)
    suspend fun updateImageIfEmpty(
        collectionId: String,
        imageUrl: String
    )

    @Query("""
        UPDATE collections 
        SET title = :title 
        WHERE id = :collectionId
    """)
    suspend fun updateName(
        collectionId: String,
        title: String
    )

    @Query("""
        UPDATE collections
        SET imageUrl = :imageUrl
        WHERE id = :collectionId
    """)
    suspend fun updateImage(
        collectionId: String,
        imageUrl: String?
    )

    @Query("""
        SELECT c.*, COUNT(cm.menuId) AS menuCount
        FROM collections c
        LEFT JOIN collection_menu cm 
            ON c.id = cm.collectionId
        GROUP BY c.id
    """)
    fun getCollectionsWithMenuCount(): Flow<List<CollectionWithMenu>>

}
