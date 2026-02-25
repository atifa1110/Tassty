package com.example.core.domain.usecase

import com.example.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRestaurantWishlistStatusUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(id: String): Flow<Boolean> =
        repository.observeIsFavorite(id)
}
