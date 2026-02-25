package com.example.core.domain.usecase

import com.example.core.domain.model.Cart
import com.example.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCartByMenuIdUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(menuId: String): Flow<Cart?> {
        return repository.observeCartByMenuId(menuId)
    }
}