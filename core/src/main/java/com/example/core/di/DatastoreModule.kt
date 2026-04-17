package com.example.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.UUID
import javax.inject.Singleton
import androidx.core.content.edit

private const val AUTH_PREFERENCES_NAME = "auth_preferences"
private const val AUTH_PREFERENCES_SECURE_NAME = "secure_auth_preferences"
private const val LOCATION_PREFERENCES_NAME = "secure_location_preferences"

private const val PASS_PREFERENCES_NAME = "secure_pass_preferences"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @AuthDataStore
    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(AUTH_PREFERENCES_NAME) }
        )
    }

//    @AuthDataStore
//    @Provides
//    @Singleton
//    fun provideAuthPath(
//        @ApplicationContext context: Context
//    ): SharedPreferences {
//        val masterKey = MasterKey.Builder(context)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        return EncryptedSharedPreferences.create(
//            context,
//            AUTH_PREFERENCES_SECURE_NAME,
//            masterKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//    }

    @LocationDataStore
    @Provides
    @Singleton
    fun provideLocationDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(LOCATION_PREFERENCES_NAME) }
        )
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
}