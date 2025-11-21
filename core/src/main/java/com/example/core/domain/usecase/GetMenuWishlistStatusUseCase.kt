package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class GetMenuWishlistStatusUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(menuId: String): Boolean {
        return collectionRepository.checkMenuWishlistStatus(menuId)
    }
}