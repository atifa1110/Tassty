package com.example.core.di

import android.content.Context
import com.example.core.data.source.remote.api.CategoryApi
import com.example.core.data.source.remote.api.MenuApi
import com.example.core.data.source.remote.api.RestaurantApi
import com.example.core.data.source.remote.api.VoucherApi
import com.example.core.data.source.remote.datasource.DummyCategoryApi
import com.example.core.data.source.remote.datasource.DummyMenuApi
import com.example.core.data.source.remote.datasource.DummyRestaurantApi
import com.example.core.data.source.remote.datasource.DummyVoucherApi
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

    @Provides
    @Singleton
    fun provideMenuApi(
        @ApplicationContext context: Context,
        gson: Gson
    ): MenuApi{
        return DummyMenuApi(context, gson)
    }

    @Provides
    @Singleton
    fun provideVoucherApi(
        @ApplicationContext context: Context,
        gson: Gson
    ): VoucherApi{
        return DummyVoucherApi(context, gson)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(
        @ApplicationContext context: Context,
        gson: Gson
    ): CategoryApi{
        return DummyCategoryApi(context, gson)
    }
}