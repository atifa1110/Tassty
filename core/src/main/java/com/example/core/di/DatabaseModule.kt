package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.source.local.dao.MenuDao
import com.example.core.data.source.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // 1. PROVIDE DATABASE INSTANCE (AppDatabase)
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "menu_collection_db"
        ).build()
    }

    // 2. PROVIDE DAO (Data Access Object)
    // Hilt akan menggunakan instance AppDatabase yang sudah dibuat di atas
    @Singleton
    @Provides
    fun provideMenuDao(database: AppDatabase): MenuDao {
        return database.menuDao()
    }
}