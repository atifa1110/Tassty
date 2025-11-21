package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class GetCollectionListIdUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(menuId: String): List<Int> {
        return collectionRepository.getCollectionIdsForMenu(menuId)
    }
}