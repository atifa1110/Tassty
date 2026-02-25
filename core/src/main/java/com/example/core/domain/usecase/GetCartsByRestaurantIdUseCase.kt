package com.example.core.domain.usecase

import com.example.core.domain.model.CartGroup
import com.example.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartsByRestaurantIdUseCase @Inject constructor(
    private val repository: CartRepository
){
    operator fun invoke(resId: String): Flow<CartGroup>{
        return repository.getCartsByRestaurantId(resId)
    }
}