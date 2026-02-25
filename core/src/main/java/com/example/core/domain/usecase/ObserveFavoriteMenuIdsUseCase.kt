package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteMenuIdsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<Set<String>> =
        collectionRepository.observeFavoriteMenuIds()
}
