package com.example.core.di

import com.example.core.data.source.local.cache.LocationManager
import com.example.core.data.source.local.datastore.LocationDataStore
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LocationInterceptor @Inject constructor(
    private val storage: LocationManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val (lat, lng) = runBlocking { storage.getLatestLocation() }

        val newUrl = chain.request().url.newBuilder()
            .addQueryParameter("lat", lat.toString())
            .addQueryParameter("lng", lng.toString())
            .build()

        val newRequest = chain.request().newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}