package com.example.core.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//// ApiModule.kt
//@Module
//@InstallIn(SingletonComponent::class)
//object ApiModule {
//
//    @Provides
//    @Singleton
//    fun provideGson(): Gson {
//        return Gson()
//    }
//
//}