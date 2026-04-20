package com.example.core.di

import android.content.Context
import androidx.core.content.edit
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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
import net.sqlcipher.database.SupportFactory
import java.util.UUID
import javax.inject.Singleton
import kotlin.text.toByteArray

private const val PASS_PREFERENCES_NAME = "secure_pass_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        @DatabasePassphrase passphrase: ByteArray
    ): AppDatabase {
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tassty_db"
        ).openHelperFactory(factory)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @DatabasePassphrase
    @Provides
    @Singleton
    fun provideDatabasePassphrase(@ApplicationContext context: Context): ByteArray {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            PASS_PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var password = sharedPreferences.getString("db_passphrase", null)
        if (password == null) {
            password = UUID.randomUUID().toString()
            sharedPreferences.edit { putString("db_passphrase", password) }
        }

        return password.toByteArray()
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