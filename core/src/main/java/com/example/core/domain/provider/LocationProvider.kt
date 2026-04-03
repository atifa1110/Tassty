package com.example.core.domain.provider

import com.example.core.domain.model.LocationDetail
import kotlinx.coroutines.flow.Flow


interface LocationProvider {

    fun getCurrentLocation(): Flow<LocationDetail>

}