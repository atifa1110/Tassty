package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.MenuBusinessInfo
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.provider.LocationProvider
import com.example.core.domain.repository.CollectionRepository
import com.example.core.domain.repository.MenuRepository
import com.example.core.domain.repository.RestaurantRepository
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getStatus
import com.example.core.domain.utils.getTodayStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSearchRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val locationProvider: LocationProvider
) {
    operator fun invoke(): Flow<TasstyResponse<List<RestaurantBusinessInfo>>> = flow {
        val result = repository.getSearchRestaurants()

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
