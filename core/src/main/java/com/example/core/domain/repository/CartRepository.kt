package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface CartRepository{

    suspend fun addToCart(menu: Menu,restaurant: Restaurant, quantity: Int,summary: String, notes: String)
    suspend fun removeCartById(cartId: String)
    suspend fun removeCartByRestaurantId(restaurantId: String)
    suspend fun updateCartQuantity(cartId: String, isIncrement: Boolean)
    fun observeCartByMenuId(menuId: String): Flow<Cart?>
    fun getAllCartWithDetails(): Flow<TasstyResponse<CartGroup>>
    fun getCartsByRestaurantId(restaurantId: String): Flow<CartGroup>
}