package com.example.tassty.util

import com.example.core.domain.model.OperationalDay
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantStatusResult
import com.example.core.ui.model.RestaurantUiModel
import com.example.core.ui.model.ReviewUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object RestaurantPreviewData {
    val categoriesUiModel = listOf(
        CategoryUiModel(id = "CAT-001", name = "Martabak", imageUrl = ""),
    ).toImmutableList()

    val emptyRestaurant = Restaurant(
        id = "",
        name = "",
        imageUrl = "",
        fullAddress = "",
        latitude = 0.0,
        longitude = 0.0,
        city = "",
        categories = emptyList(),
        rank = 0,
        distance = 0,
        rating = 0.0,
        totalReviews = 0,
        deliveryCost = 0,
        deliveryTime = "",
        isOpenFromApi = false,
        closingTimeServerFromApi = ""
    )

    val restaurants = listOf(
        Restaurant(
            id = "RES-001",
            name = "Indah Cafe",
            imageUrl = "",
            fullAddress = "Jl. Sudirman No. 10",
            latitude = -6.2088,
            longitude = 106.8456,
            city = "Jakarta",
            categories = listOf("Bakery", "Martabak", "Western"),
            rank = 0,
            distance = 4,
            rating = 4.9,
            totalReviews = 1250,
            deliveryCost = 10000,
            deliveryTime = "10-20 min",
            isOpenFromApi = true,
            closingTimeServerFromApi = "2026-01-13T15:00:00.000Z"
        ),
        Restaurant(
            id = "RES-002",
            name = "Foodie Heaven",
            imageUrl = "",
            fullAddress = "Jl. Raya Bogor No. 5",
            latitude = -6.2500,
            longitude = 106.8000,
            city = "Jakarta",
            categories = listOf("Fusion", "Nasi", "Seafood"),
            rank = 1,
            distance = 4,
            rating = 4.7,
            totalReviews = 450,
            deliveryCost = 10000,
            deliveryTime = "25-30 min",
            isOpenFromApi = false,
            closingTimeServerFromApi = "2026-01-13T15:00:00.000Z"
        ),
    )

    val restaurantUiList = restaurants.map { it.toUiModel() }
        .toImmutableList()

    val operationalHours = listOf(
        OperationalDay("Monday", "08.00 - 22.00"),
        OperationalDay("Tuesday", "08.00 - 22.00"),
        OperationalDay("Wednesday", "08.00 - 22.00"),
        OperationalDay("Thursday", "08.00 - 22.00"),
        OperationalDay("Friday", "08.00 - 22.00"),
        OperationalDay("Saturday", "08.00 - 22.00"),
        OperationalDay("Sunday", "Off Day")
    )

    val restaurantDetail = DetailRestaurantUiModel(
        id = "1",
        name = "Kopi Kenangan - Depok Town Square",
        imageUrl = "...",
        fullAddress = "jalan kemang no 5",
        city = "",
        categories = "Bakery, Martabak, Western",
        rank = 0,
        distance = 4,
        rating = 4.9,
        totalReviews = 1250,
        deliveryTime = "10-20 min",
        operationalDay = operationalHours.map { it.toUiModel("") },
        isVerified = true,
        deliveryCost = 10000,
        statusResult = RestaurantStatusResult(RestaurantStatus.CLOSED,
            "Open at 08.00 today"),
        todayHour = "08.00 - 10.00",
        formatDistance = "",
        formatRating = "4.8",
        formatReviewCount = "(+200)",
        lat = 0.0,
        lng = 0.0,
        isWishlist = false
    )

    val restaurantMenuUiModel = listOf(
        RestaurantMenuUiModel(
            restaurant = restaurants[0].toUiModel(),
            menus = MenuPreviewData.menuUiList
        )
    )

    val reviews = listOf(
        ReviewUiModel(
            id = "REV-1",
            username = "Andrew",
            date = "Sep 27, 2024",
            rating = 2,
            comment = "Indah Café offers a fantastic \ndining experience with a warm \nambiance and friendly staff.",
            profileImage = "",
            orderItems = "Hazelnut Latte, Kopi Kenangan Mantan"
        ),
        ReviewUiModel(
            id = "REV-2",
            username = "Kiana",
            date = "Sep 27, 2024",
            rating = 4,
            comment = "Indah Café offers a fantastic \ndining experience with a warm \nambiance and friendly staff.",
            profileImage = "",
            orderItems = "Hazelnut Latte, Kopi Kenangan Mantan"
        )
    ).toImmutableList()
}