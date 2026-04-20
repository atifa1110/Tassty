package com.example.core.data.source.local.datasource

import androidx.room.Transaction
import com.example.core.data.source.local.database.dao.CartDao
import com.example.core.data.source.local.database.dao.MenuDao
import com.example.core.data.source.local.database.dao.RestaurantDao
import com.example.core.data.source.local.database.entity.CartEntity
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.data.source.local.database.model.CartWithMenuAndRestaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
        price: Int,
        options: String,
        optionIds: List<String>,
        notes: String,
    ) = withContext(Dispatchers.IO) {
        val currentCartItem = cartDao.getAnyCartItem()
        if (currentCartItem != null && currentCartItem.restaurantId != restaurant.id) {
            cartDao.clearCart()
        }

        // Upsert Master Data (Restaurant & Menu)
        restaurantDao.upsert(restaurant)
        menuDao.upsert(menu)

        // Cek apakah menu dengan opsi yang sama sudah ada di cart
        val existingItem = cartDao.getCartItemByMenuIdSingle(menu.id)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                options = options,
                notes = notes,
                quantity = quantity,
                price = price
            )
            cartDao.updateCart(updatedItem)
        } else {
            cartDao.insert(
                CartEntity(
                    menuId = menu.id,
                    restaurantId = restaurant.id,
                    quantity = quantity,
                    price = price,
                    notes = notes,
                    options = options,
                    optionIds = optionIds,
                    isHidden = false
                )
            )
        }
    }

    suspend fun removeCartItem(cartId: String) {
        cartDao.deleteById(cartId)
    }

    suspend fun removeCartsByIds(cartIds: List<String>){
        cartDao.deleteMultipleCarts(cartIds)
    }

    suspend fun removeCartItemByRestaurantId(restaurantId: String) {
        cartDao.deleteByRestaurantId(restaurantId)
    }

    suspend fun removeHiddenCart() {
        cartDao.clearHiddenCart()
    }

    suspend fun updateCartQuantity(cartId: String, isIncrement: Boolean)= withContext(Dispatchers.IO) {
        if (isIncrement) {
            cartDao.incrementQuantity(cartId)
        } else {
            cartDao.decrementQuantity(cartId)
        }
    }

    suspend fun updateIsHidden(cartIds: List<String>, isHidden: Boolean) {
        cartDao.updateIsHiddenMultiple(cartIds = cartIds, isHidden = isHidden)
    }

    suspend fun updateNotes(cartId: String, notes: String) {
        cartDao.updateNotes(cartId = cartId, notes = notes)
    }

    fun observeCartByMenuId(menuId: String): Flow<CartEntity?>{
        return cartDao.observeCartItemByMenuId(menuId)
    }

    fun getAllCartWithDetails(): Flow<List<CartWithMenuAndRestaurant>> {
        return cartDao.getCartWithDetails()
    }

    fun getCartByRestaurantId(resId: String): Flow<List<CartWithMenuAndRestaurant>> {
        return cartDao.getCartWithDetails()
            .map { list ->
                list.filter { it.cart.restaurantId == resId }
            }
    }
}