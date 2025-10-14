package com.example.core.data.source.remote.datasource

import com.example.core.data.model.MenuDto
import com.example.core.data.source.remote.api.MenuApi
import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.safeApiCall
import javax.inject.Inject

class MenuNetworkDataSource @Inject constructor(
    private val service: MenuApi
){
    suspend fun getRecommendedMenus() : ResultWrapper<List<MenuDto>>{
        return safeApiCall {
            service.getRecommendedMenus()
        }
    }

    suspend fun getSuggestedMenus() : ResultWrapper<List<MenuDto>>{
        return safeApiCall {
            service.getSuggestedMenus()
        }
    }
}