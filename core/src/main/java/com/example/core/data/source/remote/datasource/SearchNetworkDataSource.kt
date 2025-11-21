package com.example.core.data.source.remote.datasource

import com.example.core.data.model.FilterOptionsDto
import com.example.core.data.source.remote.api.SearchApiService
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.data.source.remote.network.safeApiCall2
import javax.inject.Inject

class SearchNetworkDataSource @Inject constructor(
    private val searchApiService: SearchApiService
) {
    suspend fun filterOption(): TasstyResponse<FilterOptionsDto> {
        return safeApiCall2{
           searchApiService.getFilterOption()
        }
    }
}