package com.example.core.di

import android.content.Context
import com.example.core.data.source.remote.api.AuthApiService
import com.example.core.data.source.remote.api.CategoryApiService
import com.example.core.data.source.remote.api.MenuApiService
import com.example.core.data.source.remote.api.RestaurantApiService
import com.example.core.data.source.remote.api.SearchApiService
import com.example.core.data.source.remote.api.UserApiService
import com.example.core.data.source.remote.api.VoucherApiService
import com.example.core.domain.model.Menu
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(context)) // read json file
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummy.base.url/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    fun provideCategoryApi(retrofit: Retrofit): CategoryApiService =
        retrofit.create(CategoryApiService::class.java)

    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    fun provideRestaurantApi(retrofit: Retrofit): RestaurantApiService =
        retrofit.create(RestaurantApiService::class.java)

    @Provides
    fun provideMenuApi(retrofit: Retrofit): MenuApiService =
        retrofit.create(MenuApiService::class.java)

    @Provides
    fun provideVoucherApi(retrofit: Retrofit): VoucherApiService =
        retrofit.create(VoucherApiService::class.java)

    @Provides
    fun provideSearchApi(retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)
}
