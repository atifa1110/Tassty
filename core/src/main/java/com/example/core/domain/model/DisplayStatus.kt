package com.example.core.domain.model

interface DisplayStatus {
    val isEnabled: Boolean
}

enum class RestaurantStatus : DisplayStatus {
    OPEN { override val isEnabled = true },
    CLOSED { override val isEnabled = false },
    OFFDAY { override val isEnabled = false }
}

enum class MenuStatus : DisplayStatus {
    AVAILABLE { override val isEnabled = true },
    SOLDOUT { override val isEnabled = false },
    CLOSED { override val isEnabled = false }
}