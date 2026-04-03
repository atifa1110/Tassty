package com.example.core.data.source.local.datasource

import com.example.core.data.source.local.datastore.AuthDataStore
import com.google.gson.annotations.Since
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthLocalDataSource @Inject constructor(
    private val authDataStore: AuthDataStore
) {
    suspend fun getAccessToken(): String? {
        return authDataStore.authStatus.map { it.accessToken }.firstOrNull()
    }

    suspend fun getRefreshToken(): String? {
        return authDataStore.authStatus.map { it.refreshToken }.firstOrNull()
    }
}