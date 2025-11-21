package com.example.core.data.source.remote.api

import com.example.core.data.model.FilterOptionsDto
import com.example.core.data.source.remote.network.BaseResponse
import retrofit2.http.GET

interface SearchApiService {

    @GET("filter_option")
    suspend fun getFilterOption(
    ): BaseResponse<FilterOptionsDto>

}