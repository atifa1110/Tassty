package com.example.core.domain.usecase


import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class InitializeSystemCollectionsUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke() {
        return repository.initializeSystemCollections()
    }
}
