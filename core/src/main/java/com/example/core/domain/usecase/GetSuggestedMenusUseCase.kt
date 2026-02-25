package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.CollectionRepository
import com.example.core.domain.repository.MenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSuggestedMenusUseCase @Inject constructor(
    private val menuRepository: MenuRepository,
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<Menu>>> {
        return combine(
            menuRepository.getSuggestedMenus(),
            collectionRepository.observeFavoriteMenuIds()
        ) { menuResponse, favoriteIds ->
            when (menuResponse) {
                is TasstyResponse.Success -> {
                    val menusWithWishlist = menuResponse.data?.map { menu ->
                        menu.copy(isWishlist = favoriteIds.contains(menu.id))
                    }
                    TasstyResponse.Success(menusWithWishlist, menuResponse.meta)
                }
                is TasstyResponse.Error -> menuResponse
                is TasstyResponse.Loading -> TasstyResponse.Loading
            }
        }.flowOn(Dispatchers.Default)
    }
}