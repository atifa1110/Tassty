package com.example.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "collection_menu_cross_ref",
    primaryKeys = ["collectionId", "menuId"],
    foreignKeys = [
        ForeignKey(entity = MenuCollectionEntity::class,
            parentColumns = ["collectionId"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE),
       ForeignKey(entity = MenuEntity::class,
            parentColumns = ["menuId"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class CollectionMenuCrossRef(
    val collectionId: Int,
    val menuId: Int
)