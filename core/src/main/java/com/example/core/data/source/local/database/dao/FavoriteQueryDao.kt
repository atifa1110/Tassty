package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.data.source.local.database.model.MenuWithRestaurant
import com.example.core.data.source.local.database.model.RestaurantWithFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteQueryDao {

    @Transaction
    @Query("""
        SELECT *
        FROM menus
        WHERE id IN (
            SELECT menuId
            FROM collection_menu
            WHERE collectionId = :collectionId
        )
    """)
    fun getFavoriteMenus(
        collectionId: String
    ): Flow<List<MenuWithRestaurant>>
}
