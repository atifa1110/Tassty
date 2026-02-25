package com.example.core.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "collection_menu",
    primaryKeys = ["collectionId", "menuId"],
    indices = [
        Index("collectionId"),
        Index("menuId"),
        Index("restaurantId")
    ]
)
data class CollectionMenuCrossRef(
    val collectionId: String,
    val restaurantId: String,
    val menuId: String
)
