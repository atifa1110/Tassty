package com.example.core.domain.model

// D. View Model: Data untuk tampilan list Koleksi
data class CollectionListItem(
    val collectionId: Int,
    val name: String,
    val menuCount: Int,
    val firstItemImageUrl: String?
)
