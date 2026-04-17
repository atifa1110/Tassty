package com.example.core.data.repository

import android.util.Log
import com.example.core.data.source.local.database.entity.CartEntity
import com.example.core.data.source.local.datasource.CartDataSource
import com.example.core.data.source.local.mapper.toDatabase
import com.example.core.data.source.local.mapper.toDomain
import com.example.core.data.source.local.mapper.toSingleCartDomain
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Cart
import com.example.core.domain.model.CartGroup
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dataSource: CartDataSource
) : CartRepository{

    override suspend fun addToCart(
        menu: Menu,
        restaurant: Restaurant,
        quantity: Int,
        price: Int,
        summary: String,
        notes: String
    ) {
        val menuEntity = menu.toDatabase(restaurant.id)
        val restaurantEntity = restaurant.toDatabase()
        return dataSource.addToCart(menuEntity,restaurantEntity,
            quantity,price,summary,notes
        )
    }

    override suspend fun removeCartById(cartId: String) {
        return dataSource.removeCartItem(cartId)
    }

    override suspend fun removeCartsByIds(cartIds: List<String>) {
        return dataSource.removeCartsByIds(cartIds)
    }

    override suspend fun removeCartByRestaurantId(restaurantId: String) {
        return dataSource.removeCartItemByRestaurantId(restaurantId)
    }

    override suspend fun removeHiddenCart() {
        return dataSource.removeHiddenCart()
    }

    override suspend fun updateCartQuantity(cartId: String, isIncrement: Boolean) {
        return dataSource.updateCartQuantity(cartId,isIncrement)
    }

    override suspend fun updateCartIsHidden(
        cartId: List<String>,
        isHidden: Boolean
    ) {
        return dataSource.updateIsHidden(cartId,isHidden)
    }

    override suspend fun updateCartNotes(cartId: String, notes: String) {
        return dataSource.updateNotes(cartId,notes)
    }

    override fun observeCartByMenuId(menuId: String): Flow<Cart?> {
        return dataSource.observeCartByMenuId(menuId).map { it?.toDomain() }
    }

    override fun getAllCartWithDetails(): Flow<CartGroup>  {
        return dataSource.getAllCartWithDetails()
            .map { entities -> entities.toSingleCartDomain() }
    }

    override fun getCartsByRestaurantId(restaurantId: String): Flow<CartGroup> {
        return dataSource.getCartByRestaurantId(restaurantId).map { it.toSingleCartDomain() }
    }

}