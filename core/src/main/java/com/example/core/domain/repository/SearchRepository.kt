package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.FilterOptions
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun filterOption(): Flow<TasstyResponse<FilterOptions>>
}