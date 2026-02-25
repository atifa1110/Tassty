package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class UpdateCollectionNameUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collectionId: String,name: String){
        return collectionRepository.updateCollectionName(collectionId,name)
    }
}