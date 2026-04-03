package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.LocationDetail
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getCurrentLocationDetail(): LocationDetail
    suspend fun getAddressFromCoordinate(lat: Double, lng: Double): String
}