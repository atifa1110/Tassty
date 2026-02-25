package com.example.core.domain.usecase

import com.example.core.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFavoriteRestaurantUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(restaurantId: String){
        return repository.removeFavorite(restaurantId)
    }
}