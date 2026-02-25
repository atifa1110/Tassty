package com.example.core.domain.usecase

import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsMenuFavoriteUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    operator fun invoke(menuId: String) : Flow<Boolean>{
        return repository.observeIsMenuFavorite(menuId)
    }
}