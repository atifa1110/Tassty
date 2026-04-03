package com.example.core.di

import com.example.core.data.repository.AuthRepositoryImpl
import com.example.core.data.source.local.datasource.AuthLocalDataSource
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val dataSource: AuthLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            try {
                withTimeout(3000) {
                    dataSource.getAccessToken()
                }
            } catch (e: Exception) {
                null
            }
        }

        android.util.Log.d("Token",token.toString())
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(request)
    }
}