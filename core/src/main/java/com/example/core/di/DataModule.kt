package com.example.core.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // 1. Menyediakan Geocoder
    @Provides
    @Singleton
    fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
        // Geocoder memerlukan Context sebagai parameter constructor
        return Geocoder(context)
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    // 2. Dependensi lain seperti Retrofit, Room Database, dll., juga disediakan di sini
}