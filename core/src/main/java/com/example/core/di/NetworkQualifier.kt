package com.example.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocationClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainClient