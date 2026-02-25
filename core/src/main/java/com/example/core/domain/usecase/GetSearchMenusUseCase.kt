package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchMenusUseCase @Inject constructor(
    private val repository: MenuRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<Menu>>> = flow {
        val result = repository.getSearchMenus()

        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.data, result.meta))
                }

                is TasstyResponse.Error -> emit(result)
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}