package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class RemoveMenuFromCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(
        menuId: String,
        collectionIds: List<String>
    ) {
        collectionRepository.removeWishlist(
            menuId = menuId,
            collectionIds = collectionIds
        )
    }
}
