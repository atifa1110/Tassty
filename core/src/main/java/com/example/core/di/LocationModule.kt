package com.example.core.di

import com.example.core.data.provider.DefaultLocationProvider
import com.example.core.domain.provider.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        defaultLocationProvider: DefaultLocationProvider
    ): LocationProvider // Hilt akan memberikan DefaultLocationProvider ketika diminta LocationProvider
}
