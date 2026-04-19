package com.example.tassty.util

import com.example.core.domain.model.AddressType
import com.example.core.domain.model.UserAddress
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.mapper.FilterIconKeys
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.CardUserUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.tassty.R
import com.example.tassty.model.ChipFilterOption
import kotlinx.collections.immutable.toImmutableList

object UserData {
    val addressesDomain = listOf(
        UserAddress(
            id = "address_1",
            fullAddress = "Jl. Sudirman No. 12, Kebayoran Baru, Jakarta Selatan",
            latitude = -6.2235,
            longitude = 106.8119,
            addressName = "Apartment 12A",
            landmarkDetail = "Near the fountain, 3rd floor, unit 12A",
            addressType = AddressType.PERSONAL,
            isPrimary = true,
        ),
        UserAddress(
            id = "address_2",
            fullAddress = "Komplek Perkantoran Mega Kuningan Blok E, Jakarta Selatan",
            latitude = -6.2274,
            longitude = 106.8378,
            addressName = "Head Office - Sinar Raya",
            landmarkDetail = "Gedung Tower 3, lantai 15 (samping lift B)",
            addressType = AddressType.PERSONAL,
            isPrimary = false,
        )
    )
    val addresses = addressesDomain.map { it.toUiModel() }.toImmutableList()

    val historyOptions = listOf(
        ChipFilterOption("Chicken", R.drawable.history),
        ChipFilterOption("KFC", R.drawable.history),
        ChipFilterOption("Falafel", R.drawable.history)
    )

    val popularOptions = listOf(
        ChipFilterOption("Chicken", R.drawable.history),
        ChipFilterOption("KFC", R.drawable.history),
        ChipFilterOption("Janji Jiwa", R.drawable.history),
        ChipFilterOption("Falafel", R.drawable.history),
        ChipFilterOption("Banana", R.drawable.history),
        ChipFilterOption("Flower", R.drawable.history)
    )

    val defaultFilter = listOf(
        FilterOptionUi(
            key = "DEFAULT_SORT",
            label = "Sort",
            category = FilterCategory.SORT,
            iconRes = FilterIconKeys.SORT
        ),
        FilterOptionUi(
            key = "DEFAULT_RATING",
            label = "Rating",
            category = FilterCategory.RATING,
            iconRes = FilterIconKeys.STAR
        ),
        FilterOptionUi(
            key = "DEFAULT_MODE",
            label = "Mode",
            category = FilterCategory.MODE,
            iconRes = FilterIconKeys.DELIVERY
        ),
        FilterOptionUi(
            key = "DEFAULT_PRICE",
            label = "Price",
            category = FilterCategory.PRICE,
            iconRes = FilterIconKeys.PRICE
        ),
        FilterOptionUi(
            key = "DEFAULT_CUISINE",
            label = "Cuisine",
            category = FilterCategory.CUISINE
        )
    )

    val cardList = listOf(
        CardUserUiModel(
            id = "CARD-001",
            stripeId = "pm_123444243",
            maskedNumber = "**** **** **** 4242",
            cardBrand = "visa",
            expDate = "11/28",
            cardholderName = "Rafiq Daniel",
            themeColor = "pink",
            themeBackground = "pattern_1",
            isActive = true,
            isSelected = true,
            isSwipeActionVisible = true
        ),
        CardUserUiModel(
            id = "CARD-002",
            stripeId = "pm_1323242",
            maskedNumber = "**** **** **** 4242",
            cardBrand = "mastercard",
            expDate = "11/28",
            cardholderName = "Rafiq Daniel",
            themeColor = "orange",
            themeBackground = "pattern_3",
            isActive = true,
            isSelected = false,
            isSwipeActionVisible = false
        )
    ).toImmutableList()
}