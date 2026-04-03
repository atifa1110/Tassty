package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.source.local.database.dao.CartDao
import com.example.core.data.source.local.database.dao.CleanupDao
import com.example.core.data.source.local.database.dao.CollectionDao
import com.example.core.data.source.local.database.dao.CollectionMenuDao
import com.example.core.data.source.local.database.dao.FavoriteRestaurantDao
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.dao.NotificationDao
import com.example.core.data.source.local.database.dao.RestaurantDao
import com.example.core.data.source.local.database.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tassty_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideRestaurantDao(database: AppDatabase): RestaurantDao {
        return database.restaurantDao()
    }

    @Singleton
    @Provides
    fun provideMenuDao(database: AppDatabase): MenuDao {
        return database.menuDao()
    }

    @Singleton
    @Provides
    fun provideCollectionMenuDao(database: AppDatabase): CollectionMenuDao{
        return database.collectionMenuDao()
    }

    @Singleton
    @Provides
    fun provideCollectionDao(database: AppDatabase): CollectionDao {
        return database.collectionDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteRestaurantDao(database: AppDatabase): FavoriteRestaurantDao {
        return database.favoriteRestaurantDao()
    }

    @Singleton
    @Provides
    fun provideCleanupDao(database: AppDatabase): CleanupDao {
        return database.cleanupDao()
    }

    @Singleton
    @Provides
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Singleton
    @Provides
    fun provideNotificationDao(database: AppDatabase): NotificationDao {
        return database.notificationDao()
    }
}