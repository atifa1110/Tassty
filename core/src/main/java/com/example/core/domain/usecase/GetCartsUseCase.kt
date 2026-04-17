package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.CartGroup
import com.example.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke() : Flow<CartGroup>{
        return cartRepository.getAllCartWithDetails()
    }
}