package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CleanupDao {
        // Menghapus menu yang tidak ada di keranjang DAN tidak ada di koleksi manapun
        @Query("""
        DELETE FROM menus 
        WHERE id NOT IN (SELECT menuId FROM cart) 
        AND id NOT IN (SELECT menuId FROM collection_menu)
    """)
        suspend fun deleteOrphanedMenus()

        // Menghapus restoran yang tidak ada di keranjang DAN tidak ada di koleksi manapun
        @Query("""
        DELETE FROM restaurants 
        WHERE id NOT IN (SELECT restaurantId FROM cart) 
        AND id NOT IN (SELECT restaurantId FROM collection_menu)
    """)
        suspend fun deleteOrphanedRestaurants()
}