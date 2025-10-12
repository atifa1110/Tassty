package com.example.tassty.model

// Domain Model
data class Restaurant(
    val id: String,
    val name: String,
    val imageUrl: String,
    val category: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTime: String,
    val rank: Int? = null,
    val locationDetails: LocationDetails,
    val operationalHours: List<OperationalDay>,
){
    val cityName: String
        get() = locationDetails.city
}

// UI Model
data class RestaurantUiModel(
    val restaurant: Restaurant,
    val distance: Int?,
    val operationalStatus: RestaurantStatus
){
    val formattedCategories: String
        get() = restaurant.category.joinToString(separator = ", ")

    val formattedDistance: String
        get() = distance?.let { meters ->
            if (meters < 1000) "$meters m" else "%.1f km".format(meters / 1000.0)
        } ?: "N/A"

}

data class LocationDetails(
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val city: String,           
    val postcode: String? = null
)

enum class RestaurantStatus {
    OPEN,
    CLOSED,
    OFFDAY
}

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