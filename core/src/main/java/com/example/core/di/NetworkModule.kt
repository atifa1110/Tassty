package com.example.core.di

import com.example.core.data.source.remote.api.AuthAuthenticatedApiService
import com.example.core.data.source.remote.api.CategoryApiService
import com.example.core.data.source.remote.api.DetailApiService
import com.example.core.data.source.remote.api.MenuApiService
import com.example.core.data.source.remote.api.OrderApiService
import com.example.core.data.source.remote.api.AuthPublicApiService
import com.example.core.data.source.remote.api.CategoryLocationApiService
import com.example.core.data.source.remote.api.DetailLocationApiService
import com.example.core.data.source.remote.api.RestaurantApiService
import com.example.core.data.source.remote.api.ReviewApiService
import com.example.core.data.source.remote.api.SearchApiService
import com.example.core.data.source.remote.api.SearchLocationApiService
import com.example.core.data.source.remote.api.UserApiService
import com.example.core.data.source.remote.api.VoucherApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

    private const val BASE_URL = "http://10.0.2.2:3000/"

    @Singleton
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    @AuthClient
    fun provideAuthOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @LocationClient
    fun provideLocationOkHttpClient(
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

    @Singleton
    @Provides
    @MainClient
    fun provideMainOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    @AuthClient
    fun provideAuthRetrofit(@AuthClient client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @LocationClient
    fun provideLocationRetrofit(@LocationClient client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @MainClient
    fun provideMainRetrofit(@MainClient client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    fun provideAuthPublicApi(@AuthClient retrofit: Retrofit): AuthPublicApiService =
        retrofit.create(AuthPublicApiService::class.java)

    @Provides
    fun provideAuthAuthenticatedApi(@MainClient retrofit: Retrofit): AuthAuthenticatedApiService =
        retrofit.create(AuthAuthenticatedApiService::class.java)

    @Provides
    fun provideCategoryApi(@MainClient retrofit: Retrofit): CategoryApiService =
        retrofit.create(CategoryApiService::class.java)

    @Provides
    fun provideCategoryLocationApi(@LocationClient retrofit: Retrofit): CategoryLocationApiService =
        retrofit.create(CategoryLocationApiService::class.java)

    @Provides
    fun provideUserApi(@MainClient retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    fun provideRestaurantApi(@LocationClient retrofit: Retrofit): RestaurantApiService =
        retrofit.create(RestaurantApiService::class.java)

    @Provides
    fun provideMenuApi(@LocationClient retrofit: Retrofit): MenuApiService =
        retrofit.create(MenuApiService::class.java)

    @Provides
    fun provideVoucherApi(@MainClient retrofit: Retrofit): VoucherApiService =
        retrofit.create(VoucherApiService::class.java)

    @Provides
    fun provideSearchApi(@MainClient retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)

    @Provides
    fun provideSearchLocationApi(@LocationClient retrofit: Retrofit): SearchLocationApiService=
        retrofit.create(SearchLocationApiService::class.java)

    @Provides
    fun provideDetailApi(@MainClient retrofit: Retrofit): DetailApiService =
        retrofit.create(DetailApiService::class.java)

    @Provides
    fun provideDetailLocationApi(@LocationClient retrofit: Retrofit): DetailLocationApiService =
        retrofit.create(DetailLocationApiService::class.java)

    @Provides
    fun provideReviewApi(@MainClient retrofit: Retrofit): ReviewApiService =
        retrofit.create(ReviewApiService::class.java)

    @Provides
    fun provideOrderApi(@MainClient retrofit: Retrofit): OrderApiService =
        retrofit.create(OrderApiService::class.java)
}
