package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.LocationDetails
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getTodayStatus
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRecommendedRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val locationProvider: LocationProvider
) {
    suspend operator fun invoke(): ResultWrapper<List<RestaurantBusinessInfo>> {
        Log.d("Diana", "GetRecommendedRestaurantsUseCase started")
        val result = repository.getRecommendedRestaurants()

        return when (result) {
            is ResultWrapper.Success -> {
                // dapatkan lokasi di IO (lokasi biasanya I/O-ish)
                // 1. Dapatkan lokasi (dengan penanganan error)
                val userLocation = try {
                    withContext(Dispatchers.IO) { locationProvider.getCurrentLocation() }
                } catch (e: Exception) {
                    Log.e("Diana", "Error getting location", e)
                    // Jika gagal, set null dan lanjutkan penanganan
                    LocationDetails("",0.0,0.0,"")
                }

                Log.d("Diana", "After getting location: $userLocation")
                // lakukan mapping/komputasi berat di Default
                val businessList = withContext(Dispatchers.Default) {
                    result.data.map { restaurant ->
                        val status = restaurant.getTodayStatus() // CPU-bound? put here
                        val distance = calculateHaversine(userLocation, restaurant.locationDetails)
                        RestaurantBusinessInfo(restaurant, distance, status)
                    }
                }

                ResultWrapper.Success(businessList, result.meta)
            }
            is ResultWrapper.Error -> {
                Log.d("Diana", "Error: ${result.meta}")
                ResultWrapper.Error(result.meta)

            }
            is ResultWrapper.Loading -> {
                Log.d("Diana", "Loading Only")
                ResultWrapper.Loading
            }
        }
    }
}
