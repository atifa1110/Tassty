package com.example.core.data.source.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String? = null,
    val isSystem: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
