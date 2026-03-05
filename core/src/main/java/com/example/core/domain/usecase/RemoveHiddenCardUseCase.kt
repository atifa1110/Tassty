package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class RemoveHiddenCardUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(){
        return repository.removeHiddenCart()
    }
}