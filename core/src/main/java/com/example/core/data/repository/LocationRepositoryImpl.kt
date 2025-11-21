package com.example.core.data.repository

import com.example.core.data.model.Resource
import com.example.core.data.provider.DefaultLocationProvider
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.LocationDetails
import com.example.core.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val provider: DefaultLocationProvider
) : LocationRepository {

    override suspend fun getCurrentLocation(): Flow<TasstyResponse<LocationDetails>> = flow {
        emit(TasstyResponse.Loading)

        try {
            val location = provider.getCurrentLocation()
            emit(
                TasstyResponse.Success(
                    data = location,
                    meta = Meta(
                        code = 200,
                        status = "success",
                        message = "Location fetched successfully"
                    )
                )
            )
        } catch (e: Exception) {
            emit(
                TasstyResponse.Error(
                    meta = Meta(
                        code = 500,
                        status = "failed",
                        message = e.message ?: "Unknown error occurred"
                    )
                )
            )
        }
    }
}
