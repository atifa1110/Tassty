package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class SaveMenuToCollectionUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(
        menuId: String,
        collectionIds: List<Int>
    ): ResultWrapper<String> {
        return collectionRepository.saveMenuToCollections(menuId,collectionIds)
    }
}