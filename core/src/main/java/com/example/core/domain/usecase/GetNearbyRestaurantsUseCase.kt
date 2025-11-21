package com.example.core.domain.usecase

import android.util.Log
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getTodayStatus
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.provider.LocationProvider
import com.example.core.domain.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNearbyRestaurantsUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val locationProvider: LocationProvider
) {
    operator fun invoke(): Flow<TasstyResponse<List<RestaurantBusinessInfo>>> = flow {
        val result = repository.getNearbyRestaurants()

        result.collect { result ->
            when(result){
                is TasstyResponse.Success -> {
                    val userLocation = withContext(Dispatchers.IO) {
                        locationProvider.getCurrentLocation()
                    }

                    val businessList = withContext(Dispatchers.Default) {
                        result.data?.map { restaurant ->
                            val status = restaurant.getTodayStatus() // CPU-bound? put here
                            val distance = calculateHaversine(userLocation, restaurant.locationDetails)
                            RestaurantBusinessInfo(restaurant, distance, status)
                        }?.filter { it.distance != null && it.distance <= 5000 } // filter ≤ 5km
                            ?.sortedBy { it.distance }
                            ?.take(10)
                    }

                    emit(TasstyResponse.Success(businessList,result.meta))
                }
                is TasstyResponse.Error -> emit(result) // just pass error downstream
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}