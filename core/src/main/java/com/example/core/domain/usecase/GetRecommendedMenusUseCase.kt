package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.MenuBusinessInfo
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.repository.CollectionRepository
import com.example.core.domain.repository.MenuRepository
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getStatus
import com.example.core.domain.utils.getTodayStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRecommendedMenusUseCase @Inject constructor(
    private val repository: MenuRepository,
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<MenuBusinessInfo>>> = flow {
        val result = repository.getRecommendedMenus()

        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    val businessList = result.data
                        ?.map { menu ->
                            val restaurantStatus = menu.getTodayStatus()
                            val status = menu.getStatus(restaurantStatus)
                            val isWishlist = collectionRepository.checkMenuWishlistStatus(menu.id)
                            MenuBusinessInfo(menu, isWishlist, status)
                        }

                    emit(TasstyResponse.Success(businessList, result.meta))
                }

                is TasstyResponse.Error -> emit(result) // just pass error downstream
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}