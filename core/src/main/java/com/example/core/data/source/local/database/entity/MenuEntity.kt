package com.example.core.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "menus",
    indices = [Index("restaurantId")]
)
data class MenuEntity(
    @PrimaryKey val id: String,
    val restaurantId: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: Int
)
