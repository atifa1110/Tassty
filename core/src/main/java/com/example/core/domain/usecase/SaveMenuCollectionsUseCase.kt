package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.local.mapper.toDatabase
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Restaurant
import com.example.core.domain.repository.CollectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveMenuCollectionsUseCase @Inject constructor(
    private val repository: CollectionRepository
) {
    suspend operator fun invoke(
        menu: Menu,
        collectionIdsFromUser: List<String>
    ) = withContext(Dispatchers.IO) {

        val existingCollectionIds = repository.getCollectionIdsByMenu(menu.id).toSet()
        val selectedCollectionIds = collectionIdsFromUser.toSet()

        val toAdd = selectedCollectionIds - existingCollectionIds
        val toRemove = existingCollectionIds - selectedCollectionIds

        val menuEntity = menu.toDatabase(menu.restaurant.id)
        val restaurantEntity = menu.restaurant.toDatabase()

        if(toAdd.isNotEmpty()){
            repository.addWishlist(menuEntity,restaurantEntity,toAdd.toList())
        }

        if(toRemove.isNotEmpty()){
            repository.removeWishlist(menu.id,toRemove.toList())
        }
    }
}