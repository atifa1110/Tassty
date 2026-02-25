package com.example.core.data.source.remote.datasource

import android.util.Log
import com.example.core.data.model.CategoryDto
import com.example.core.data.model.DetailMenuDto
import com.example.core.data.model.DetailRestaurantDto
import com.example.core.data.source.remote.api.DetailApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class DetailNetworkDataSource @Inject constructor(
    private val service: DetailApiService
){
    suspend fun getDetailRestaurant(id: String): TasstyResponse<DetailRestaurantDto> {
        return safeApiCall { service.getDetailRestaurant(id) }
    }

    suspend fun getDetailMenu(id : String): TasstyResponse<DetailMenuDto> {
        return safeApiCall {
            service.getDetailMenu(id)
        }
    }
}