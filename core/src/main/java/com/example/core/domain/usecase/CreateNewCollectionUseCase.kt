package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class CreateNewCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collectionName: String){
        return collectionRepository.addCollection(collectionName)
    }
}