package com.example.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "collections")
data class MenuCollectionEntity(
    @PrimaryKey(autoGenerate = true) val collectionId: Int = 0, // Dibuat otomatis oleh Room
    val name: String
)

data class CollectionWithMenusAndRestaurants(
    @Embedded val collection: MenuCollectionEntity,
    @Relation(
        parentColumn = "collectionId",
        entity = CollectionMenuCrossRef::class,
        associateBy = Junction(CollectionMenuCrossRef::class),
        entityColumn = "menuId",
        projection = ["menuId", "name", "price", "imageUrl", "restaurantId"] // Ambil properti MenuItem yang dibutuhkan
    )
    val menuItems: List<MenuItemWithRestaurant> // Data class baru untuk menggabungkan Menu dan Restoran
)

data class MenuItemWithRestaurant(
    @Embedded val menuItem: MenuEntity,
    @Relation(
        parentColumn = "restaurantId", // Dari MenuItemEntity
        entityColumn = "restaurantId" // Dari RestaurantEntity
    )
    val restaurant: RestaurantEntity
)