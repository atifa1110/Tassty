package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailMenu
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface DetailRepository {

    fun getDetailRestaurant(restId: String): Flow<TasstyResponse<DetailRestaurant>>

    fun getDetailRoute(restId: String): Flow<TasstyResponse<Route>>

    fun getDetailBestSellerMenu(restId:String): Flow<TasstyResponse<List<Menu>>>

    fun getDetailRecommendedMenu(restId:String): Flow<TasstyResponse<List<Menu>>>

    fun getDetailAllMenu(restId:String): Flow<TasstyResponse<List<Menu>>>

    fun getDetailMenu(menuId: String): Flow<TasstyResponse<DetailMenu>>
}