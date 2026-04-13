package com.example.core.data.source.remote.datasource

import android.util.Log
import com.example.core.data.model.CategoryDto
import com.example.core.data.model.DetailMenuDto
import com.example.core.data.model.DetailRestaurantDto
import com.example.core.data.model.MenuDto
import com.example.core.data.model.RouteDto
import com.example.core.data.source.remote.api.DetailApiService
import com.example.core.data.source.remote.api.DetailLocationApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class DetailNetworkDataSource @Inject constructor(
    private val locationService: DetailLocationApiService,
    private val service: DetailApiService
){
    suspend fun getDetailRestaurant(restId: String): TasstyResponse<DetailRestaurantDto> {
        return safeApiCall { locationService.getDetailRestaurant(restId = restId) }
    }

    suspend fun getDetailRoute(restId : String): TasstyResponse<RouteDto> {
        return safeApiCall {
            locationService.getDetailRoutes(restId = restId)
        }
    }

    suspend fun getDetailBestSellerMenu(restId: String) : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getDetailBestSellerMenu(restId = restId)
        }
    }

    suspend fun getDetailRecommendedMenu(restId: String) : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getDetailRecommendedMenu(restId = restId)
        }
    }

    suspend fun getDetailAllMenu(restId: String) : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getDetailAllMenu(restId = restId)
        }
    }

    suspend fun getDetailMenu(menuId : String): TasstyResponse<DetailMenuDto> {
        return safeApiCall {
            locationService.getDetailMenu(menuId = menuId)
        }
    }
}