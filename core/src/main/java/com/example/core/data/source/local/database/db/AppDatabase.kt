package com.example.core.data.source.local.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.database.entity.MenuCollectionEntity

@Database(
    entities = [
        MenuCollectionEntity::class,
        CollectionMenuCrossRef::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
}