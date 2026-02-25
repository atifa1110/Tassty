package com.example.core.domain.usecase

import com.example.core.domain.repository.LocationRepository
import javax.inject.Inject

class SyncLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {

    suspend operator fun invoke(){
        return locationRepository.syncLocation()
    }
}