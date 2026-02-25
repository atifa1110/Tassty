package com.example.core.data.source.local.database.model

import androidx.room.Embedded
import com.example.core.data.source.local.database.entity.CollectionEntity

data class CollectionWithMenu(
    @Embedded val collection: CollectionEntity,
    val menuCount: Int
)
