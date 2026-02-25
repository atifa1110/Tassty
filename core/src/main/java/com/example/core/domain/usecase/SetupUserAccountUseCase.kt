package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.UserAddress
import com.example.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetupUserAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(address: UserAddress, cuisines: List<String>) : Flow<TasstyResponse<String>> = flow {
        authRepository.setupAccount(address,cuisines).collect { result ->
            when(result){
                is TasstyResponse.Error ->  emit(TasstyResponse.Error(result.meta))
                is TasstyResponse.Success-> {
                    emit(TasstyResponse.Success(result.data, result.meta))
                }
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}