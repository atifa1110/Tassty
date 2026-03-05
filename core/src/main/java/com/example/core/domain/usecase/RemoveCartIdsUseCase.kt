package com.example.core.domain.usecase

import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class RemoveCartIdsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(cartIds: List<String>){
        return repository.removeCartsByIds(cartIds)
    }
}