package com.example.core.domain.usecase

import com.example.core.domain.model.CollectionRestaurantWithMenu
import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionMenuByIdUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(collectionId: String) : Flow<List<CollectionRestaurantWithMenu>>{
        return collectionRepository.getMenuCollectionById(collectionId)
    }
}