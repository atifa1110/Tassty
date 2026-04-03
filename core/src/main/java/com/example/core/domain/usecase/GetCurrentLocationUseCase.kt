package com.example.core.domain.usecase

import com.example.core.domain.model.LocationDetail
import com.example.core.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(): LocationDetail {
        return repository.getCurrentLocationDetail()
    }
}
