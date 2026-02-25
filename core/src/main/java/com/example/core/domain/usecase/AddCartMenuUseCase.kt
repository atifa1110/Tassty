package com.example.core.domain.usecase

import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class AddCartMenuUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(menu: Menu, restaurant: Restaurant, quantity: Int,
                        summary: String, notes: String){
        return cartRepository.addToCart(menu,restaurant,quantity,summary,notes)
    }
}