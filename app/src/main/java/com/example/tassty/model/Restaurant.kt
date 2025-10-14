package com.example.tassty.model

// Data class sederhana untuk menampung data ulasan
data class Review(
    val id : Int,
    val userName: String,
    val date: String,
    val rating: Int,
    val reviewText: String,
    val profilePicture: String // Gunakan Int untuk resource ID gambar
)

// 1. Data Class untuk Opsi Individual (Chicken Thigh, Eggs, dll.)
data class MenuItemOption(
    val id: String,
    val name: String,
    val priceAddon: Int,
)

// 2. Data Class untuk Seluruh Bagian Pilihan (Choice of protein, Choice of dressing)
data class MenuChoiceSection(
    val id : String,
    val title: String,
    val subtitle: String = "pick 1",
    val isRequired: Boolean,
    val minSelection: Int,
    val maxSelection: Int,
    val options: List<MenuItemOption>,
    val selectedOptions: List<MenuItemOption> = emptyList(),
)