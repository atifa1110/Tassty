package com.example.core.data.source.local.cache

import com.example.core.data.model.RestaurantDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantCache @Inject constructor() : BaseCache<RestaurantDto>() {

    suspend fun saveRestaurants(key: String, restaurants: List<RestaurantDto>) {
        super.saveAll(key, restaurants) { it.id }
    }
}
