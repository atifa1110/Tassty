package com.example.core.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.core.data.source.local.database.entity.CartEntity
import com.example.core.data.source.local.database.model.CartWithMenuAndRestaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cart: CartEntity)

    // Mengambil satu data saja untuk dicek restaurantId-nya
    @Query("SELECT * FROM cart LIMIT 1")
    suspend fun getAnyCartItem(): CartEntity?

    // Ambil semua item di keranjang
    @Transaction
    @Query("SELECT * FROM cart")
    fun getAllCartWithDetails(): Flow<List<CartWithMenuAndRestaurant>>

    // Untuk di ViewModel (UI Sync / Auto-Centang)
    @Query("SELECT * FROM cart WHERE menuId = :menuId LIMIT 1")
    fun observeCartItemByMenuId(menuId: String): Flow<CartEntity?> // Tanpa suspend

    // Untuk di UseCase/Logic (Internal Check)
    @Query("SELECT * FROM cart WHERE menuId = :menuId LIMIT 1")
    suspend fun getCartItemByMenuIdSingle(menuId: String): CartEntity? // Pakai suspend, tanpa Flow

    // Update jumlah (quantity) secara manual
//    @Query("UPDATE cart SET quantity = :quantity WHERE cartId = :cartId")
//    suspend fun updateQuantity(cartId: String, quantity: Int)
    @Update
    suspend fun updateCart(cart: CartEntity)

    // Hapus satu item dari keranjang
    @Query("DELETE FROM cart WHERE cartId = :id")
    suspend fun deleteById(id: String)

    // Hapus semua item by restarant id
    @Query("DELETE FROM cart WHERE restaurantId = :id")
    suspend fun deleteByRestaurantId(id: String)

    @Query("UPDATE cart SET quantity = quantity + 1 WHERE cartId = :id")
    suspend fun incrementQuantity(id: String)

    @Query("UPDATE cart SET quantity = CASE WHEN quantity > 1 THEN quantity - 1 ELSE 1 END WHERE cartId = :id")
    suspend fun decrementQuantity(id: String)

    // Hapus semua isi keranjang
    @Query("DELETE FROM cart")
    suspend fun clearCart()
}