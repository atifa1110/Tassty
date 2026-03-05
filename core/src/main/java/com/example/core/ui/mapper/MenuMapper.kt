package com.example.core.ui.mapper

import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuWithWishlist
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.MenuUiModel

fun Menu.toUiModel(isWishlist: Boolean) : MenuUiModel{
    return MenuUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        price = this.price,
        soldCount = this.soldCount,
        rank = this.rank,
        customizable = this.customizable,
        isAvailable = this.isAvailable,
        maxQuantity = this.maxQuantity,
        menuStatus = this.menuStatus,
        restaurant = this.restaurant.toUiModel(),
        isWishlist = isWishlist,
    )
}

fun MenuWithWishlist.toUiModel() : MenuUiModel{
    return MenuUiModel(
        id = this.menu.id,
        name = this.menu.name,
        imageUrl = this.menu.imageUrl,
        description = this.menu.description,
        price = this.menu.price,
        soldCount = this.menu.soldCount,
        rank = this.menu.rank,
        customizable = this.menu.customizable,
        isAvailable = this.menu.isAvailable,
        maxQuantity = this.menu.maxQuantity,
        menuStatus = this.menu.menuStatus,
        restaurant = this.menu.restaurant.toUiModel(),
        isWishlist = isWishlist,
    )
}

fun Menu.toUiModel(): MenuUiModel{
    return MenuUiModel(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        price = this.price,
        soldCount = this.soldCount,
        rank = this.rank,
        customizable = this.customizable,
        isAvailable = this.isAvailable,
        maxQuantity = this.maxQuantity,
        menuStatus = this.menuStatus,
        restaurant = this.restaurant.toUiModel(),
        isWishlist = this.isWishlist
    )
}

fun MenuUiModel?.toDomain(): Menu{
    return Menu(
        id = this?.id?:"",
        name = this?.name?:"",
        imageUrl = this?.imageUrl?:"",
        description = this?.description?:"",
        price = this?.price?:0,
        soldCount = this?.soldCount?:0,
        rank = this?.rank?:0,
        customizable = this?.customizable?:false,
        isAvailable = this?.isAvailable?:false,
        maxQuantity = this?.maxQuantity?:0,
        stockLabel = "",
        stockStatus = "",
        restaurant = this?.restaurant?.toDomain()?: empty
    )
}

fun MenuUiModel?.toDomain(restaurant: DetailRestaurantUiModel): Menu{
    return Menu(
        id = this?.id?:"",
        name = this?.name?:"",
        imageUrl = this?.imageUrl?:"",
        description = this?.description?:"",
        price = this?.price?:0,
        soldCount = this?.soldCount?:0,
        rank = this?.rank?:0,
        customizable = this?.customizable?:false,
        isAvailable = this?.isAvailable?:false,
        maxQuantity = this?.maxQuantity?:0,
        stockLabel = "",
        stockStatus = "",
        restaurant = restaurant.toDomain()
    )
}

fun List<Menu>.enrichWithWishlistAndStatus(
    detailData: DetailRestaurant?,
    favIds: Set<String>
): List<Menu> {
    return this.map { menu ->
        menu.copy(
            restaurant = menu.restaurant.copy(
                isOpenFromApi = detailData?.isOpen ?: menu.restaurant.isOpenFromApi,
                closingTimeServerFromApi = detailData?.closingTimeServer ?: menu.restaurant.closingTimeServerFromApi
            ),
            isWishlist = favIds.contains(menu.id)
        )
    }
}