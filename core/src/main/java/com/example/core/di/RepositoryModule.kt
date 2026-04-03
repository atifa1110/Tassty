package com.example.core.di

import com.example.core.data.repository.AuthRepositoryImpl
import com.example.core.data.repository.CartRepositoryImpl
import com.example.core.data.repository.CategoryRepositoryImpl
import com.example.core.data.repository.ChatRepositoryImpl
import com.example.core.data.repository.CollectionRepositoryImpl
import com.example.core.data.repository.DetailRepositoryImpl
import com.example.core.data.repository.FavoriteRepositoryImpl
import com.example.core.data.repository.LocationRepositoryImpl
import com.example.core.data.repository.MenuRepositoryImpl
import com.example.core.data.repository.NotificationRepositoryImpl
import com.example.core.data.repository.OrderRepositoryImpl
import com.example.core.data.repository.RestaurantRepositoryImpl
import com.example.core.data.repository.ReviewRepositoryImpl
import com.example.core.data.repository.SearchRepositoryImpl
import com.example.core.data.repository.UserRepositoryImpl
import com.example.core.data.repository.VoucherRepositoryImpl
import com.example.core.domain.repository.AuthRepository
import com.example.core.domain.repository.CartRepository
import com.example.core.domain.repository.CategoryRepository
import com.example.core.domain.repository.ChatRepository
import com.example.core.domain.repository.CollectionRepository
import com.example.core.domain.repository.DetailRepository
import com.example.core.domain.repository.FavoriteRepository
import com.example.core.domain.repository.LocationRepository
import com.example.core.domain.repository.MenuRepository
import com.example.core.domain.repository.NotificationRepository
import com.example.core.domain.repository.OrderRepository
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.repository.ReviewRepository
import com.example.core.domain.repository.SearchRepository
import com.example.core.domain.repository.UserRepository
import com.example.core.domain.repository.VoucherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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


    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository


    @Binds
    @Singleton
    abstract fun bindCollectionRepository(
        collectionRepositoryImpl: CollectionRepositoryImpl
    ): CollectionRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindDetailRepository(
        detailRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository


    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

}
