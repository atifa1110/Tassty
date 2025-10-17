package com.example.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.data.source.local.entity.CollectionWithMenusAndRestaurants
import com.example.core.data.source.local.entity.MenuCollectionEntity

@Dao
interface MenuDao {

    // 1. FUNGSI UNTUK MENYIMPAN (INSERT)
    @Insert
    suspend fun insertCollection(collection: MenuCollectionEntity): Long // Mengembalikan ID yang baru dibuat

    // 2. FUNGSI UNTUK MENGAMBIL DATA (GET)
    // Query ini akan mengembalikan View Data yang sudah kita buat: Koleksi + Menu + Restoran
    @Transaction
    @Query("SELECT * FROM collections WHERE collectionId = :id")
    fun getCollectionDetail(id: Int): CollectionWithMenusAndRestaurants

    // 3. FUNGSI UNTUK MENGAMBIL SEMUA DAFTAR KOLEKSI
    @Query("SELECT * FROM collections")
    fun getAllCollections(): List<MenuCollectionEntity>

}