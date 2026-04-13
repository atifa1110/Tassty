package com.example.core.data.repository

import com.example.core.data.mapper.toDomain
import com.example.core.data.source.remote.datasource.DetailNetworkDataSource
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailMenu
import com.example.core.domain.model.DetailRestaurant
import com.example.core.domain.model.Menu
import com.example.core.domain.model.Route
import com.example.core.domain.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val dataSource: DetailNetworkDataSource
) : DetailRepository{

    override fun getDetailRestaurant(restId: String): Flow<TasstyResponse<DetailRestaurant>> = flow {
        emit(TasstyResponse.Loading())

        val response = dataSource.getDetailRestaurant(restId)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailRoute(restId: String): Flow<TasstyResponse<Route>> = flow {
        emit(TasstyResponse.Loading())

        val response = dataSource.getDetailRoute(restId)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailBestSellerMenu(restId:String): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = dataSource.getDetailBestSellerMenu(restId)
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailRecommendedMenu(restId: String): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = dataSource.getDetailRecommendedMenu(restId)
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailAllMenu(restId: String): Flow<TasstyResponse<List<Menu>>> = flow{
        emit(TasstyResponse.Loading())

        val result = dataSource.getDetailAllMenu(restId)
        when (result) {
            is TasstyResponse.Success -> {
                val menus = result.data?.map { menuDto -> menuDto.toDomain() }
                emit(TasstyResponse.Success(menus, result.meta))
            }
            is TasstyResponse.Error -> emit(result)
            is TasstyResponse.Loading -> emit(result)
        }
    }.flowOn(Dispatchers.IO)

    override fun getDetailMenu(menuId: String): Flow<TasstyResponse<DetailMenu>> = flow {
        emit(TasstyResponse.Loading())

        val response = dataSource.getDetailMenu(menuId)
        when(response){
            is TasstyResponse.Error -> emit(TasstyResponse.Error(response.meta))
            is TasstyResponse.Success -> {
                emit(TasstyResponse.Success(response.data?.toDomain(), response.meta))
            }
            else -> {}
        }
    }.flowOn(Dispatchers.IO)
}