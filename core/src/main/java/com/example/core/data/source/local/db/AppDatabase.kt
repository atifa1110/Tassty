package com.example.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.source.local.dao.MenuDao
import com.example.core.data.source.local.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.entity.MenuCollectionEntity
import com.example.core.data.source.local.entity.MenuEntity
import com.example.core.data.source.local.entity.RestaurantEntity

@Database(
    entities = [
        RestaurantEntity::class,
        MenuEntity::class,
        MenuCollectionEntity::class,
        CollectionMenuCrossRef::class
    ],
    version = 1 // Versi database
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao

}