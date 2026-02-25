package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartId: String, isIncrement: Boolean) {
        repository.updateCartQuantity(cartId, isIncrement)
    }
}