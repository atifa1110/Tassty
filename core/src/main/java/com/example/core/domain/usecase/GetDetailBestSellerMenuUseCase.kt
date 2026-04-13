package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.CollectionRepository
import com.example.core.domain.repository.DetailRepository
import com.example.core.domain.repository.MenuRepository
import com.example.core.ui.mapper.enrichWithWishlistAndStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDetailBestSellerMenuUseCase @Inject constructor(
    private val detailRepository: DetailRepository,
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(restaurantId: String): Flow<TasstyResponse<List<Menu>>> {
        return combine(
            detailRepository.getDetailBestSellerMenu(restaurantId),
            detailRepository.getDetailRestaurant(restaurantId),
            collectionRepository.observeFavoriteMenuIds()
        ) { menuRes, restoRes, favIds ->
            val detailData = (restoRes as? TasstyResponse.Success)?.data
            when (menuRes) {
                is TasstyResponse.Success -> {
                    val menuWithWishlist = menuRes.data?.enrichWithWishlistAndStatus(detailData, favIds)
                    TasstyResponse.Success(menuWithWishlist, menuRes.meta)
                }
                is TasstyResponse.Error -> TasstyResponse.Error(menuRes.meta)
                is TasstyResponse.Loading -> TasstyResponse.Loading()
            }
        }.flowOn(Dispatchers.Default)
    }
}