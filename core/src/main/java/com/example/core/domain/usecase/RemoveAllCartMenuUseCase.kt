package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class RemoveAllCartMenuUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(restaurantId: String){
        repository.removeCartByRestaurantId(restaurantId)
    }
}