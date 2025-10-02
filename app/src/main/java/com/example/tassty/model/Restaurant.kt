package com.example.tassty.model

data class Restaurant(
    val id: Int,
    val name: String,
    val isVerified: Boolean,
    val rating: Double,
    val deliveryTime: String,
    val deliveryCost: String,
    val imageUrl: String,
    val city: String,
    val distance: Int,
    val operationalHours: List<OperationalDay>,
    val menus: List<Menu> = emptyList()
)
enum class RestaurantStatus { OPEN, CLOSED, OFFDAY }

data class RestaurantStatusResult(
    val status: RestaurantStatus,
    val message: String
)

data class OperationalDay(
    val day: String,
    val hours: String,
    val isToday: Boolean = false
)

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
    val name: String,
    val priceAddon: Int,
)

// 2. Data Class untuk Seluruh Bagian Pilihan (Choice of protein, Choice of dressing)
data class MenuChoiceSection(
    val title: String,
    val subtitle: String = "pick 1",
    val isRequired: Boolean,
    val minSelection: Int,
    val maxSelection: Int,
    val options: List<MenuItemOption>,
    val selectedOptions: List<MenuItemOption> = emptyList()
)