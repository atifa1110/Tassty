package com.example.core.data.source.local.mapper

import com.example.core.data.source.local.database.entity.CollectionEntity
import com.example.core.data.source.local.database.entity.MenuEntity
import com.example.core.data.source.local.database.entity.RestaurantEntity
import com.example.core.data.source.local.database.model.CollectionWithMenu
import com.example.core.data.source.local.database.model.MenuWithRestaurant
import com.example.core.domain.model.Collection
import com.example.core.domain.model.CollectionMenu
import com.example.core.domain.model.CollectionRestaurant
import com.example.core.domain.model.CollectionRestaurantWithMenu

fun CollectionEntity.toDomain(): Collection{
    return Collection(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl?:"",
        menuCount = 0,
        isSelected = false
    )
}

fun CollectionWithMenu.toDomain(): Collection{
    return Collection(
        id = this.collection.id,
        title = this.collection.title,
        imageUrl = this.collection.imageUrl?:"",
        menuCount = this.menuCount,
        isSelected = false
    )
}

fun MenuEntity.toDomain(): CollectionMenu{
    return CollectionMenu(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        description = this.description,
        price = this.price
    )
}
fun RestaurantEntity.toDomain(): CollectionRestaurant{
    return CollectionRestaurant(
        id = this.id,
        name = this.name,
        rating = this.rating,
        city = this.city
    )
}

fun List<MenuWithRestaurant>.toGroupedDomain(): List<CollectionRestaurantWithMenu> {
    return this
        .groupBy { it.restaurant.id }
        .map { (_, items) ->
            CollectionRestaurantWithMenu(
                restaurant = CollectionRestaurant(
                    id = items.first().restaurant.id,
                    name = items.first().restaurant.name,
                    rating = items.first().restaurant.rating,
                    city = items.first().restaurant.city
                ),
                menus = items.map { item ->
                    CollectionMenu(
                        id = item.menu.id,
                        name = item.menu.name,
                        imageUrl = item.menu.imageUrl,
                        description = item.menu.description,
                        price = item.menu.price
                    )
                }
            )
        }
}
