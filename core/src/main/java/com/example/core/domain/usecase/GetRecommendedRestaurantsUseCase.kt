package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getTodayStatus
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.provider.LocationProvider
import javax.inject.Inject

class GetRecommendedRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(): ResultWrapper<List<RestaurantBusinessInfo>> {
        val result = repository.getRecommendedRestaurants()

        return when (result) {
            is ResultWrapper.Success -> {
                val userLocation = locationProvider.getCurrentLocation()
                val businessList = result.data.map { restaurant ->
                    val status = restaurant.getTodayStatus()
                    val distance = calculateHaversine(userLocation, restaurant.locationDetails)
                    RestaurantBusinessInfo(restaurant, distance, status)
                }
                ResultWrapper.Success(businessList, result.meta)
            }
            is ResultWrapper.Error -> ResultWrapper.Error(result.meta)
            is ResultWrapper.Loading -> ResultWrapper.Loading
        }
    }
}
