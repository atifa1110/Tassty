package com.example.core.di

import android.content.Context
import com.example.core.data.source.remote.api.AuthApiService
import com.example.core.data.source.remote.api.CategoryApiService
import com.example.core.data.source.remote.api.DetailApiService
import com.example.core.data.source.remote.api.MenuApiService
import com.example.core.data.source.remote.api.RestaurantApiService
import com.example.core.data.source.remote.api.ReviewApiService
import com.example.core.data.source.remote.api.SearchApiService
import com.example.core.data.source.remote.api.UserApiService
import com.example.core.data.source.remote.api.VoucherApiService
import com.example.core.domain.model.Menu
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        locationInterceptor: LocationInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(locationInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
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

    @Provides
    fun provideDetailApi(retrofit: Retrofit): DetailApiService =
        retrofit.create(DetailApiService::class.java)

    @Provides
    fun provideReviewApi(retrofit: Retrofit): ReviewApiService =
        retrofit.create(ReviewApiService::class.java)
}
