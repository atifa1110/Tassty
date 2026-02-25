package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class RemoveCartMenuUseCase @Inject constructor(
    private val repository: CartRepository
){
    suspend operator fun invoke(cartId: String){
        return repository.removeCartById(cartId)
    }
}