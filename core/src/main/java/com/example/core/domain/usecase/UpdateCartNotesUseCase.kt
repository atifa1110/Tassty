package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartNotesUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartId: String, notes: String){
        return repository.updateCartNotes(cartId,notes)
    }
}