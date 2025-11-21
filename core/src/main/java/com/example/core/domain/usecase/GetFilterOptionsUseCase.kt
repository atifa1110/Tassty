package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.FilterOptions
import com.example.core.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterOptionsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke() : Flow<TasstyResponse<FilterOptions>>{
        return searchRepository.filterOption()
    }
}