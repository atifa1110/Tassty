package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class GetUserCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke() = collectionRepository.getCollectionListForView()
}