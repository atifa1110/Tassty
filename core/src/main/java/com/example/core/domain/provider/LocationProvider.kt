package com.example.core.domain.provider

import com.example.core.domain.model.LocationDetails

interface LocationProvider {
    suspend fun getCurrentLocation(): LocationDetails
}