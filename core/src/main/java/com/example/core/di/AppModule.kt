package com.example.core.di

import com.example.core.utils.FilterManager
import com.example.core.utils.FilterManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindFilterManager(
        filterManagerImpl: FilterManagerImpl
    ): FilterManager
}