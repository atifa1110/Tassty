package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartHiddenUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartIds: List<String>, isHidden: Boolean){
        return repository.updateCartIsHidden(cartId = cartIds, isHidden = isHidden)
    }
}