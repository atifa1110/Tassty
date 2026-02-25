package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCollectionsByIdUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(menuId: String) : List<String> =
        withContext(Dispatchers.IO) {
            collectionRepository.getCollectionIdsByMenu(menuId)
        }
}