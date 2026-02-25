package com.example.core.domain.model

data class Menu(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: Int,
    val soldCount: Int,
    val rank: Int,
    val customizable: Boolean,
    val isAvailable: Boolean,
    val maxQuantity: Int,
    val stockStatus: String,
    val stockLabel: String,
    val restaurant: Restaurant,
    val isWishlist: Boolean = false
){
    val menuStatus: MenuStatus
        get() = getMenuStatus(isAvailable, restaurant.statusResult.status)
}

fun getMenuStatus(isAvailable: Boolean, restaurantStatus: RestaurantStatus): MenuStatus {
    return when {
        restaurantStatus != RestaurantStatus.OPEN -> MenuStatus.CLOSED   // restoran tutup
        isAvailable -> MenuStatus.AVAILABLE                              // menu habis / disable
        else -> MenuStatus.SOLDOUT                                  // resto buka, menu ready
    }
}

data class MenuWithWishlist(
    val menu: Menu,
    val isWishlist: Boolean,
)

