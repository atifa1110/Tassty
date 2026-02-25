package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class RemoveCollectionByIdUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(collectionId: String){
        repository.deleteCollectionById(collectionId)
    }
}