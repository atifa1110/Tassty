package com.example.core.data.repository

import com.example.core.data.provider.DefaultLocationProvider
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.LocationDetail
import com.example.core.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val provider: DefaultLocationProvider
) : LocationRepository {

    override fun getCurrentLocation(): Flow<LocationDetail> {
        return provider.getCurrentLocation()
    }

    override suspend fun syncLocation() {
        return provider.syncLocation()
    }
}
