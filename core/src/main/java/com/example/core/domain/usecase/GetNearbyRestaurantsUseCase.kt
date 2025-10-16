package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getTodayStatus
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.provider.LocationProvider
import com.example.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNearbyRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(): ResultWrapper<List<RestaurantBusinessInfo>> {
        Log.d("Diana", "GetNearbyRestaurantsUseCase started")

        val result = repository.getNearbyRestaurants()
        Log.d("Diana", "Repository returned: $result")

        return when (result) {
            is ResultWrapper.Success -> {
                val userLocation = withContext(Dispatchers.IO) {
                    Log.d("Diana", "Fetching user location...")
                    locationProvider.getCurrentLocation()
                }
                Log.d("Diana", "User location: $userLocation")

                // lakukan mapping/komputasi berat di Default
                val businessList = withContext(Dispatchers.Default) {
                    result.data.map { restaurant ->
                        val status = restaurant.getTodayStatus() // CPU-bound? put here
                        val distance = calculateHaversine(userLocation, restaurant.locationDetails)
                        RestaurantBusinessInfo(restaurant, distance, status)
                        }
                        .filter { it.distance != null && it.distance <= 5000 } // filter ≤ 5km
                        .sortedBy { it.distance }
                        .take(10)
                }
                Log.d("Diana", "Business list mapped: ${businessList.size}")
                ResultWrapper.Success(businessList, result.meta)
            }
            is ResultWrapper.Error -> ResultWrapper.Error(result.meta)
            is ResultWrapper.Loading -> ResultWrapper.Loading
        }
    }
}