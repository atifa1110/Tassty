package com.example.tassty.model

data class FavoriteCollection(
    val collectionId: String,
    val name: String,             // Contoh: "Favorite Salad"
    val itemCount: Int,           // Contoh: 2 menus
    // Properti Baru: URL gambar untuk thumbnail koleksi.
    val thumbnailUrl: String,     // Gambar menu pertama di koleksi
    // Properti State: Status checkbox di dialog.
    val isSelected: Boolean = false
)