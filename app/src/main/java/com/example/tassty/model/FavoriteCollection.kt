package com.example.tassty.model

data class CollectionUiItem(
    val collectionId: String,
    val name: String,
    val itemCount: Int,
    val thumbnailUrl: String,
    val isSelected: Boolean = false
)