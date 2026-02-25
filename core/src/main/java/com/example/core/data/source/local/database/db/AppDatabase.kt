package com.example.core.data.source.local.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.source.local.database.dao.CartDao
import com.example.core.data.source.local.database.dao.CleanupDao
import com.example.core.data.source.local.database.dao.CollectionDao
import com.example.core.data.source.local.database.dao.CollectionMenuDao
import com.example.core.data.source.local.database.dao.FavoriteQueryDao
import com.example.core.data.source.local.database.dao.FavoriteRestaurantDao
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.dao.RestaurantDao
import com.example.core.data.source.local.database.entity.CartEntity
import com.example.core.data.source.local.database.entity.CollectionEntity
import com.example.core.data.source.local.database.entity.CollectionMenuCrossRef
import com.example.core.data.source.local.database.entity.FavoriteRestaurantEntity
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity

@Database(
    entities = [
        RestaurantEntity::class,
        MenuEntity::class,
        CollectionEntity::class,
        CollectionMenuCrossRef::class,
        FavoriteRestaurantEntity::class,
        CartEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun menuDao(): MenuDao
    abstract fun collectionMenuDao(): CollectionMenuDao
    abstract fun favoriteQueryDao(): FavoriteQueryDao
    abstract fun collectionDao() : CollectionDao
    abstract fun cleanupDao() : CleanupDao
    abstract fun favoriteRestaurantDao() : FavoriteRestaurantDao
    abstract fun cartDao() : CartDao
}