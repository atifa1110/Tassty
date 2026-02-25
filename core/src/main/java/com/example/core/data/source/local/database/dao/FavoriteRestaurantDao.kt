package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.source.local.database.entity.FavoriteRestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRestaurantDao {

    // 3️⃣ Add to favorite
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(
        entity: FavoriteRestaurantEntity
    )

    // 4️⃣ Remove from favorite
    @Query("""
        DELETE FROM favorite_restaurants 
        WHERE restaurantId = :restaurantId
    """)
    suspend fun deleteById(restaurantId: String)

    // 1️⃣ Detail screen — cek ada / nggak (reactive)
    @Query("""
        SELECT EXISTS(
            SELECT 1 
            FROM favorite_restaurants 
            WHERE restaurantId = :restaurantId
        )
    """)
    fun observeIsFavorite(restaurantId: String): Flow<Boolean>

    // 2️⃣ List screen — ambil semua ID
    @Query("""
        SELECT restaurantId 
        FROM favorite_restaurants
    """)
    fun observeFavoriteIds(): Flow<List<String>>

    @Query("""
        SELECT * 
        FROM favorite_restaurants
    """)
    fun getFavorites(): Flow<List<FavoriteRestaurantEntity>>

}
