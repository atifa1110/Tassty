package com.example.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
}