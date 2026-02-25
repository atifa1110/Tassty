package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Collection
import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<Collection>>> {
        return collectionRepository.getCollections()
    }
}