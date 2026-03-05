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

    // get all cart where is hidden true
    @Transaction
    @Query("SELECT * FROM cart WHERE isHidden = 0")
    fun getCartWithDetails(): Flow<List<CartWithMenuAndRestaurant>>

    // Untuk di ViewModel (UI Sync / Auto-Centang)
    @Query("SELECT * FROM cart WHERE menuId = :menuId LIMIT 1")
    fun observeCartItemByMenuId(menuId: String): Flow<CartEntity?>

    // Untuk di UseCase/Logic (Internal Check)
    @Query("SELECT * FROM cart WHERE menuId = :menuId LIMIT 1")
    suspend fun getCartItemByMenuIdSingle(menuId: String): CartEntity?

    @Update
    suspend fun updateCart(cart: CartEntity)

    @Query("UPDATE cart SET isHidden = :isHidden WHERE cartId IN (:cartIds)")
    suspend fun updateIsHiddenMultiple(cartIds: List<String>, isHidden: Boolean)

    // Hapus satu item dari keranjang
    @Query("DELETE FROM cart WHERE cartId = :id")
    suspend fun deleteById(id: String)

    // Room otomatis akan looping list cartIds ini ke dalam query IN (:cartIds)
    @Query("DELETE FROM cart WHERE cartId IN (:cartIds)")
    suspend fun deleteMultipleCarts(cartIds: List<String>)

    // Hapus semua item by restaurant id
    @Query("DELETE FROM cart WHERE restaurantId = :id")
    suspend fun deleteByRestaurantId(id: String)

    @Query("UPDATE cart SET quantity = quantity + 1 WHERE cartId = :id")
    suspend fun incrementQuantity(id: String)

    @Query("UPDATE cart SET quantity = CASE WHEN quantity > 1 THEN quantity - 1 ELSE 1 END WHERE cartId = :id")
    suspend fun decrementQuantity(id: String)

    // Hapus semua isi keranjang
    @Query("DELETE FROM cart")
    suspend fun clearCart()

    // Hapus semua isi keranjang
    @Query("DELETE FROM cart WHERE isHidden = 1")
    suspend fun clearHiddenCart()
}