package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.repository.DetailRepository
import com.example.core.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetDetailRestaurantUseCase @Inject constructor(
    private val repository: DetailRepository,
    private val favoriteRepository: FavoriteRepository
){
    operator fun invoke(id: String) : Flow<TasstyResponse<DetailRestaurant>>{
        return combine(
            repository.getDetailRestaurant(id),
            favoriteRepository.observeIsFavorite(id)
        ){ result , favorite ->
            when(result){
                is TasstyResponse.Success -> {
                    val detailWithWishlist = result.data?.copy(
                        isWishlist = favorite
                    )
                    TasstyResponse.Success(detailWithWishlist,result.meta)
                }
                is TasstyResponse.Error -> result
                is TasstyResponse.Loading -> TasstyResponse.Loading
            }
        }
    }
}