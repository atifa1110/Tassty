package com.example.core.di

import com.example.core.data.repository.MenuRepositoryImpl
import com.example.core.data.repository.RestaurantRepositoryImpl
import com.example.core.data.repository.VoucherRepositoryImpl
import com.example.core.domain.repository.MenuRepository
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.repository.VoucherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Binds
    @Singleton
    abstract fun bindMenuRepository(
        menuRepositoryImpl: MenuRepositoryImpl
    ): MenuRepository

    @Binds
    @Singleton
    abstract fun bindVoucherRepository(
        voucherRepositoryImpl: VoucherRepositoryImpl
    ): VoucherRepository


}
