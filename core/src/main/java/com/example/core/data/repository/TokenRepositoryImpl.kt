package com.example.core.data.repository

import com.example.core.data.source.local.datastore.AuthDataStore
import com.example.core.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore
) : TokenRepository{

    override val accessToken: Flow<String?> =
        authDataStore.authStatus.map{ it.accessToken }

    override val refreshToken: Flow<String?> =
        authDataStore.authStatus.map { it.refreshToken }

}