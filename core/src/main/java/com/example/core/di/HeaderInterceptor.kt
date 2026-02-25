package com.example.core.di

import com.example.core.data.repository.AuthRepositoryImpl
import com.example.core.data.repository.TokenRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenRepositoryImpl: TokenRepositoryImpl
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenRepositoryImpl.accessToken.first()
        }

        android.util.Log.d("AUTH_DEBUG", "Token yang ditemukan: '$token'")
        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(request)
    }
}