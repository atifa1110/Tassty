package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.DetailMenu
import com.example.core.domain.repository.DetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetailMenuUseCase @Inject constructor(
    private val repository: DetailRepository
) {
    operator fun invoke(id:String): Flow<TasstyResponse<DetailMenu>> = flow {
        val result = repository.getDetailMenu(id)

        result.collect { result ->
            when(result){
                is TasstyResponse.Success -> {
                    emit(TasstyResponse.Success(result.data,result.meta))
                }
                is TasstyResponse.Error -> emit(result)
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}