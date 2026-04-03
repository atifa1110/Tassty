package com.example.core.domain.usecase

import com.example.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetAddressCoordinateUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(lat: Double, lng: Double): String{
        return repository.getAddressFromCoordinate(lat,lng)
    }
}