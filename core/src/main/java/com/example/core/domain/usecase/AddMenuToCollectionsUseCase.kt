package com.example.core.domain.usecase

import com.example.core.data.source.local.mapper.toDatabase
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.CollectionRepository
import javax.inject.Inject

class AddMenuToCollectionsUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(
        menu: Menu, restaurant: Restaurant, collectionIds: List<String>
    ) {
        val menuEntity = menu.toDatabase(restaurant.id)
        val restaurantEntity = restaurant.toDatabase()
        collectionRepository.addWishlist(
            menu = menuEntity,
            restaurant = restaurantEntity,
            collectionIds = collectionIds
        )
    }
}
