package com.example.tassty

import com.example.tassty.model.LocationDetails
import com.example.tassty.model.Restaurant
import com.example.tassty.model.RestaurantUiModel

val restaurants = listOf(
    Restaurant(
        id = "1",
        name = "Indah Cafe",
        imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
        category = listOf("Bakery", "Martabak", "Western"),
        rating = 4.9,
        reviewCount = 1250, // Nilai reviewCount diisi
        deliveryTime = "10-20 min",
        locationDetails = LocationDetails( // city dan koordinat dipindahkan ke sini
            fullAddress = "Jl. Sudirman No. 10",
            latitude = -6.2088,
            longitude = 106.8456,
            city = "Jakarta"
        ),
        operationalHours = operationalHours
    ),
    Restaurant(
        id = "2",
        name = "Foodie Heaven",
        imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
        category = listOf("Fusion", "Nasi", "Seafood"),
        rating = 4.7,
        reviewCount = 450,
        deliveryTime = "25-30 min",
        locationDetails = LocationDetails(
            fullAddress = "Jl. Raya Bogor No. 5",
            latitude = -6.2500,
            longitude = 106.8000,
            city = "Jakarta"
        ),
        operationalHours = operationalHours
    ),
    Restaurant(
        id = "3",
        name = "Sushi Corner",
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format",
        category = listOf("Japanese", "Sushi", "Dessert"),
        rating = 4.8,
        reviewCount = 2890,
        deliveryTime = "15-25 min",
        locationDetails = LocationDetails(
            fullAddress = "Jl. Asia Afrika No. 1",
            latitude = -6.2100,
            longitude = 106.8200,
            city = "Jakarta"
        ),
        operationalHours = operationalHours
    ),
    Restaurant(
        id = "4",
        name = "Healthy Bites",
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format",
        category = listOf("Salad", "Vegan", "Minuman Sehat"),
        rating = 4.5,
        reviewCount = 980,
        deliveryTime = "20 min",
        locationDetails = LocationDetails(
            fullAddress = "Jl. HR Rasuna Said Kav. 1",
            latitude = -6.2200,
            longitude = 106.8300,
            city = "Jakarta"
        ),
        operationalHours = operationalHours
    ),
    Restaurant(
        id = "5",
        name = "Burger Factory",
        imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
        category = listOf("Burger", "American", "Fries"),
        rating = 4.6,
        reviewCount = 670,
        deliveryTime = "30 min",
        locationDetails = LocationDetails(
            fullAddress = "Jl. Kebon Jeruk IX No. 3",
            latitude = -6.2800,
            longitude = 106.7800,
            city = "Jakarta"
        ),
        operationalHours = operationalHours
    )
)

fun Restaurant.toUiModel(): RestaurantUiModel {
    val statusResult = this.getTodayStatus()
    val distance = calculateHaversine(userCurrentLocation,this.locationDetails)
    return RestaurantUiModel(
        restaurant = this,
        distance = distance,
        operationalStatus = statusResult
    )
}

val restaurantUiModel = restaurants.map { it.toUiModel() }