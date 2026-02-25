package com.example.core.domain.repository

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailMenu
import com.example.core.domain.model.DetailRestaurant
import kotlinx.coroutines.flow.Flow

interface DetailRepository {

    fun getDetailRestaurant(id: String): Flow<TasstyResponse<DetailRestaurant>>
    fun getDetailMenu(id: String): Flow<TasstyResponse<DetailMenu>>
}