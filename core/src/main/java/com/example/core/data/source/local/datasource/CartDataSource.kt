package com.example.core.data.source.local.datasource

import androidx.room.Transaction
import com.example.core.data.source.local.database.dao.CartDao
import com.example.core.data.source.local.database.dao.CleanupDao
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.dao.RestaurantDao
import com.example.core.data.source.local.database.entity.CartEntity
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.data.source.local.database.model.CartWithMenuAndRestaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartDataSource @Inject constructor(
    private val cartDao: CartDao,
    private val restaurantDao: RestaurantDao,
    private val menuDao: MenuDao
) {
    @Transaction
    suspend fun addToCart(
        menu: MenuEntity,
        restaurant: RestaurantEntity,
        quantity: Int,
        summary: String,
        notes: String
    ) {
        val currentCartItem = cartDao.getAnyCartItem()
        if (currentCartItem != null && currentCartItem.restaurantId != restaurant.id) {
            cartDao.clearCart()
        }

        // 1. Upsert Master Data (Restaurant & Menu)
        restaurantDao.upsert(restaurant)
        menuDao.upsert(menu)

        // 2. Cek apakah menu dengan opsi yang sama sudah ada di cart
        val existingItem = cartDao.getCartItemByMenuIdSingle(menu.id)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                finalSummary = summary,
                notes = notes,
                quantity = quantity
            )
            cartDao.updateCart(updatedItem)
        } else {
            cartDao.insert(
                CartEntity(
                    menuId = menu.id,
                    restaurantId = restaurant.id,
                    quantity = quantity,
                    finalSummary = summary,
                    notes = notes
                )
            )
        }
    }

    suspend fun removeCartItem(cartId: String) {
        cartDao.deleteById(cartId)
    }

    suspend fun removeCartItemByRestaurantId(restaurantId: String) {
        cartDao.deleteByRestaurantId(restaurantId)
    }

    suspend fun updateCartQuantity(cartId: String, isIncrement: Boolean) {
        if (isIncrement) {
            cartDao.incrementQuantity(cartId)
        } else {
            cartDao.decrementQuantity(cartId)
        }
    }

    fun observeCartByMenuId(menuId: String): Flow<CartEntity?>{
        return cartDao.observeCartItemByMenuId(menuId)
    }

    fun getAllCartWithDetails(): Flow<List<CartWithMenuAndRestaurant>> {
        return cartDao.getAllCartWithDetails()
    }

    fun getCartByRestaurantId(resId: String): Flow<List<CartWithMenuAndRestaurant>> {
        return cartDao.getAllCartWithDetails()
            .map { list ->
                list.filter { it.cart.restaurantId == resId }
            }
    }
}