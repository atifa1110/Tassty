package com.example.core.di

import com.example.core.data.source.local.cache.LocationManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LocationInterceptor @Inject constructor(
    private val locationManager: LocationManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val lat = locationManager.lat
        val lng = locationManager.lng

        val newUrl = chain.request().url.newBuilder()
            .apply {
                lat?.let { addQueryParameter("lat", it.toString()) }
                lng?.let { addQueryParameter("lng", it.toString()) }
            }
            .build()

        return chain.proceed(
            chain.request().newBuilder()
                .url(newUrl)
                .build()
        )
    }
}

