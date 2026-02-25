package com.example.core.data.source.remote.datasource

import com.example.core.data.model.MenuDto
import com.example.core.data.source.remote.api.MenuApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class MenuNetworkDataSource @Inject constructor(
    private val service: MenuApiService
){
    suspend fun getRecommendedMenus() : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getRecommendedMenus()
        }
    }

    suspend fun getSuggestedMenus() : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getSuggestedMenus()
        }
    }

    suspend fun getSearchMenus() : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getSearchMenus()
        }
    }

    suspend fun getDetailBestSellerMenu(id: String) : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getDetailBestSellerMenu(id)
        }
    }

    suspend fun getDetailRecommendedMenu(id: String) : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getDetailRecommendedMenu(id)
        }
    }

    suspend fun getDetailAllMenu(id: String) : TasstyResponse<List<MenuDto>>{
        return safeApiCall {
            service.getDetailAllMenu(id)
        }
    }

}