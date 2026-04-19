package com.example.tassty.util

import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.RestaurantStatus
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.OptionGroupUiModel
import com.example.core.ui.model.OptionUiModel
import com.example.core.ui.model.RestaurantStatusResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object MenuPreviewData {
    val menus = listOf(
        Menu(
            id = "MEN-011",
            name = "Shabu Premium",
            imageUrl = "...",
            description = "Lorem ipsum dolor sit amet...",
            price = 150000,
            soldCount = 120,
            rank = 1,
            isAvailable = true,
            customizable = false,
            maxQuantity = 3,
            stockStatus = "AVAILABLE",
            restaurant = RestaurantPreviewData.emptyRestaurant,
            stockLabel = ""
        ),
        Menu(
            id = "MEN-002",
            name = "Super Komplit 2",
            imageUrl = "...",
            description = "1 Pc Chicken + 1 Rice + 1 Cream Soup + 1 Mocha Float",
            price = 50000,
            soldCount = 200,
            rank = 2,
            customizable = true,
            isAvailable = true,
            maxQuantity = 10,
            stockStatus = "AVAILABLE",
            restaurant = RestaurantPreviewData.restaurants[1],
            stockLabel = ""
        )
    )

    val menuUiList: ImmutableList<MenuUiModel> =
        menus.map { it.toUiModel() }.toImmutableList()

    val optionGroups: ImmutableList<OptionGroupUiModel> = listOf(
        OptionGroupUiModel(
            id = "GRP-001",
            title = "Pilih Varian Kopi",
            required = true,
            maxPick = 1,
            options = listOf(
                OptionUiModel(id="OPT-001", name="Kenangan Blend", extraPrice = 0,
                    extraPriceText = "", isAvailable = true, isSelected = true),
                OptionUiModel(id = "OPT-002", name = "Juwara Beans", extraPrice = 0,
                    extraPriceText = "", isAvailable = false, isSelected = false)
            ),
            subtitle = ""
        )
    ).toImmutableList()

    val menuDetailItem = DetailMenuUiModel(
        id = "RES-001",
        name = "Shabu Premium Set",
        imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
        description = "shabu shabu yang enak",
        promo = true,
        priceOriginal = 150000,
        priceDiscount = 90000,
        customizable = true,
        isAvailable = true,
        maxQuantity = 10,
        optionGroups = optionGroups,
        restaurant = RestaurantPreviewData.restaurants[0].toUiModel(),
        menuStatus = MenuStatus.AVAILABLE,
        isWishlist = false
    )

    val restaurantDetailItem = DetailRestaurantUiModel(
        id = "1",
        name = "Kopi Kenangan - Depok Town Square",
        imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
        fullAddress = "jalan kemang no 5",
        city = "",
        categories = listOf("Bakery", "Martabak", "Western").joinToString(", "),
        rank = 0,
        distance = 4,
        rating = 4.9,
        totalReviews = 1250,
        deliveryTime = "10-20 min",
        operationalDay = emptyList(),
        isVerified = true,
        deliveryCost = 10000,
        statusResult = RestaurantStatusResult(RestaurantStatus.CLOSED,"Open at 08.00 today"),
        todayHour = "08.00 - 10.00",
        formatDistance = "",
        formatRating = "4.8",
        formatReviewCount = "(+200)",
        lat = 0.0,
        lng = 0.0,
        isWishlist = false
    )

}