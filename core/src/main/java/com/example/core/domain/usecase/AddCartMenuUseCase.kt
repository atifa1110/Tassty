package com.example.core.domain.usecase

import android.util.Log
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.CartRepository
import javax.inject.Inject

class AddCartMenuUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(
        menu: Menu, restaurant: Restaurant,
        quantity: Int, totalPrice:Int = 0,
        options: String,
        optionIds: List<String>,
        notes: String
    ){
        val finalPrice = if (totalPrice == 0) menu.price else totalPrice
        return cartRepository.addToCart(
            menu = menu,
            restaurant = restaurant,quantity = quantity,
            price = finalPrice, options = options,
            optionIds = optionIds,notes = notes
        )
    }
}