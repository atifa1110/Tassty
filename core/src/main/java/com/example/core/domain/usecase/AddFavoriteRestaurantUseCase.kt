package com.example.core.domain.usecase

import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteRestaurantUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(restaurant: DetailRestaurant){
        return repository.addFavorite(restaurant)
    }
}