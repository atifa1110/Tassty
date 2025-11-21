package com.example.core.data.source.local.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

// A. Entity: Collection that made by user
@Entity(tableName = "collections")
data class MenuCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val collectionId: Int = 0,
    val name: String
)

// B. Entity: Junction Table (Save data menu/restaurant)
@Entity(
    tableName = "collection_menu_cross_ref",
    primaryKeys = ["collectionId", "serverId"],
    foreignKeys = [
        ForeignKey(entity = MenuCollectionEntity::class,
            parentColumns = ["collectionId"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class CollectionMenuCrossRef(
    val collectionId: Int,
    val serverId: String,
    val name: String,
    val imageUrl: String?,
    val price: Int,
    val description: String?,
    val restaurantName: String,
    val restaurantLocation: String
)

// E. View Model: Data untuk tampilan Detail Koleksi
data class CollectionWithMenus(
    @Embedded val collection: MenuCollectionEntity,
    @Relation(
        parentColumn = "collectionId",
        entityColumn = "collectionId"
    )
    val menuItems: List<CollectionMenuCrossRef> // Items lengkap yang disimpan lokal
)