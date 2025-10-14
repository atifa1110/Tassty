package com.example.core.di

import android.content.Context
import com.example.core.data.source.remote.api.RestaurantApi
import com.example.core.data.source.remote.datasource.DummyRestaurantApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ApiModule.kt
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRestaurantApi(
        @ApplicationContext context: Context,
        gson: Gson
    ): RestaurantApi {
        return DummyRestaurantApi(context, gson)
    }
}