package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface CartRepository{

    suspend fun addToCart(menu: Menu,restaurant: Restaurant, quantity: Int, price:Int,summary: String, notes: String)
    suspend fun removeCartById(cartId: String)
    suspend fun removeCartsByIds(cartIds: List<String>)
    suspend fun removeCartByRestaurantId(restaurantId: String)
    suspend fun removeHiddenCart()
    suspend fun updateCartQuantity(cartId: String, isIncrement: Boolean)
    suspend fun updateCartIsHidden(cartId: List<String>, isHidden:Boolean)
    fun observeCartByMenuId(menuId: String): Flow<Cart?>
    fun getAllCartWithDetails(): Flow<TasstyResponse<CartGroup>>
    fun getCartsByRestaurantId(restaurantId: String): Flow<CartGroup>
}