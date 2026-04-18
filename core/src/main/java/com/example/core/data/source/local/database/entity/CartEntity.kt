package com.example.core.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.ktor.http.content.MultiPartData
import java.util.UUID

// 3. Cart Entity (Merujuk ke Menu & Restaurant)
@Entity(
    tableName = "cart",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantEntity::class,
            parentColumns = ["id"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.NO_ACTION, // Mencegah restaurant dihapus jika masih ada di cart
            onUpdate = ForeignKey.CASCADE// onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuEntity::class,
            parentColumns = ["id"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.NO_ACTION, // Mencegah menu dihapus jika masih ada di cart
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

data class CartEntity(
    @PrimaryKey val cartId: String = UUID.randomUUID().toString(),
    val menuId: String,
    val restaurantId: String,
    val quantity: Int,
    val price: Int,
    val optionIds: List<String> = emptyList(),
    val options: String?,
    val notes: String?,
    val isHidden: Boolean
)