package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.MenuBusinessInfo
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.model.RestaurantBusinessMenu
import com.example.core.domain.model.RestaurantMenu
import com.example.core.domain.model.RestaurantStatus
import com.example.core.domain.provider.LocationProvider
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getTodayStatus
import com.example.core.ui.mapper.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSearchFilterUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val locationProvider: LocationProvider
) {
    operator fun invoke() : Flow<TasstyResponse<List<RestaurantBusinessMenu>>> = flow{
        val result = restaurantRepository.getSortingRestaurants()

        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    val userLocation = withContext(Dispatchers.IO) {
                        locationProvider.getCurrentLocation()
                    }

                    val businessList = withContext(Dispatchers.Default) {
                        result.data?.map { restaurant ->
                            val status = restaurant.restaurant.getTodayStatus()
                            val distance = calculateHaversine(
                                userLocation,
                                restaurant.restaurant.locationDetails
                            )
                            val business = RestaurantBusinessInfo(restaurant.restaurant, distance, status)
                            val menuStatus = if(status == RestaurantStatus.OPEN) MenuStatus.AVAILABLE else MenuStatus.CLOSED
                            val menus = restaurant.menuList.map { it.toUiModel(menuStatus) }
                            RestaurantBusinessMenu(business, menus)
                        }
                    }

                    emit(TasstyResponse.Success(businessList, result.meta))
                }

                is TasstyResponse.Error -> emit(result) // just pass error downstream
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}