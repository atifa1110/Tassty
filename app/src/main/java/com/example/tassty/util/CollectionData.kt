package com.example.tassty.util

import com.example.core.domain.model.Collection
import com.example.core.ui.model.CollectionMenuUiModel
import com.example.core.ui.model.CollectionRestaurantUiModel
import com.example.core.ui.model.CollectionRestaurantWithMenuUiModel
import com.example.core.ui.model.CollectionUiModel
import kotlinx.collections.immutable.toImmutableList

object CollectionData {

    val collection= listOf(
        Collection(
            id = "Collection-1",
            title = "Favorite Salad",
            imageUrl = "",
            menuCount = 2,
            isSelected = true
        ),
        Collection(
            id = "Collection-2",
            title = "Daily Menus",
            imageUrl = "",
            menuCount = 1,
            isSelected = false
        )
    )

    val collectionUiModel = listOf(
        CollectionUiModel(
            id = "Collection-1",
            title = "Favorite Salad",
            imageUrl = "",
            menuCount = 2,
            isSelected = false
        ),
        CollectionUiModel(
            id = "Collection-2",
            title = "Daily Menus",
            imageUrl = "",
            menuCount = 1,
            isSelected = true
        )
    ).toImmutableList()

    val collectionMenuUiModel = listOf(
        CollectionMenuUiModel(
            id = "MEN-001",
            name = "Shabu Premium Set",
            imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
            description = "1 Pc Chicken + 1 Rice + 1 Cream Soup + 1 Mocha Float",
            priceText = "Rp150000",
        ),
        CollectionMenuUiModel(
            id = "MEN-002",
            name = "Shabu Premium Set",
            imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
            description = "shabu shabu yang enak",
            priceText = "Rp150000",
        )
    ).toImmutableList()

    val collectionRestaurantMenuUiModel = listOf(
        CollectionRestaurantWithMenuUiModel(
            restaurant = CollectionRestaurantUiModel(
                id = "RES-001",
                name = "Foodie Heaven",
                city = "Jakarta",
                ratingText = "4.7",
            ),
            menus = collectionMenuUiModel
        ),
        CollectionRestaurantWithMenuUiModel(
            restaurant = CollectionRestaurantUiModel(
                id = "RES-002",
                name = "Foodie Heaven",
                city = "Jakarta",
                ratingText = "4.7",
            ),
            menus = collectionMenuUiModel
        )
    ).toImmutableList()
}